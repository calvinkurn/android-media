//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tokopedia.instantloan.ddcollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class InfoCollectServiceImpl implements InfoCollectService {
    private List<InfoCollector> infoCollectorList = new ArrayList<>();

    public Map<String, Object> getData() throws ExecutionException, InterruptedException {
        return this.getPhoneInfo();
    }

    public void add(InfoCollector infoCollector) {
        this.infoCollectorList.add(infoCollector);
    }

    private Map<String, Object> getPhoneInfo() throws ExecutionException, InterruptedException {
        Map<String, Object> phoneInfoMap = new HashMap<>();
        for (InfoCollector infoCollector : infoCollectorList) {
            if (infoCollector == null) {
                continue;
            }
            phoneInfoMap.putAll(infoCollector.buildPhoneInfo());
        }

        return phoneInfoMap;
    }
}
