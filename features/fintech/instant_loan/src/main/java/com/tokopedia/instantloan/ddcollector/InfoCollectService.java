//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tokopedia.instantloan.ddcollector;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface InfoCollectService {
    Map<String, Object> getData() throws ExecutionException, InterruptedException;

    void add(InfoCollector var1);
}
