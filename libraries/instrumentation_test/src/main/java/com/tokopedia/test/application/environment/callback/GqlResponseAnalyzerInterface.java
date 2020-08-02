package com.tokopedia.test.application.environment.callback;

import java.util.HashMap;

public interface GqlResponseAnalyzerInterface {
    int getResponseTotalSize();
    Long getResponseTotalTime();
    Long getUserNetworkTotalDuration();
    HashMap<String, Integer> getGqlSizeMap();
    HashMap<String, Long> getGqlTimeMap();
    void reset();
}
