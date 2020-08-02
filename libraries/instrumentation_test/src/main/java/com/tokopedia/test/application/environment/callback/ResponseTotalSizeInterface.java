package com.tokopedia.test.application.environment.callback;

import java.util.HashMap;

public interface ResponseTotalSizeInterface {
    int getResponseTotalSize();
    Long getResponseTotalTime();
    HashMap<String, Integer> getGqlSizeMap();
    HashMap<String, Long> getGqlTimeMap();
    void reset();
}
