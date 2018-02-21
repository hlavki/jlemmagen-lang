/*
 * Copyright 2013 Michal Hlavac <hlavki@hlavki.eu>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.hlavki.text.jlemmagen.data;

import eu.hlavki.text.lemmagen.LemmatizerFactory;
import eu.hlavki.text.lemmagen.impl.DefaultLemmatizer;
import eu.hlavki.text.lemmagen.impl.LemmatizerSettings;
import eu.hlavki.text.lemmagen.impl.LemmatizerSettings.MsdConsideration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Michal Hlavac <hlavki@hlavki.eu>
 */
public final class TrainAll {

    private static final Logger log = LoggerFactory.getLogger(TrainAll.class);


    public static void main(String[] args) throws Exception {
        File dir = new File(".");
        if (dir.isDirectory()) {
            LemmatizerSettings settings = new LemmatizerSettings();
            settings.setUseFromInRules(false);
            settings.setMsdConsider(MsdConsideration.IGNORE);
            settings.setMaxRulesPerNode(0);
            settings.setBuildFrontLemmatizer(true);
            for (File file : dir.listFiles()) {
                if (file.getName().endsWith(".tbl")) {
                    BufferedReader br = null;
                    try {
                        br = new BufferedReader(new FileReader(file));
                        log.info("Reading " + file.getName());
                        DefaultLemmatizer lm = new DefaultLemmatizer(br, "WLM", settings);
                        log.info("Building " + file.getName());
                        lm.buildModel();
                        String filename = file.getName().substring(0, file.getName().lastIndexOf("."));
                        log.info("Saving to " + filename + ".lem");
                        LemmatizerFactory.saveToFile(lm, new File(dir, filename + ".lem"));
                    } finally {
                        try {
                            if (br != null) br.close();
                        } catch (IOException e) {
                            log.warn("Can't close stream", e);
                        }
                    }
                }
            }
        } else {
            log.error(dir.getAbsolutePath() + " is not directory");
        }
    }
}
