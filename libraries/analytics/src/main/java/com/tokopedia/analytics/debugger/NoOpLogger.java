package com.tokopedia.analytics.debugger;

import com.tokopedia.analytics.debugger.domain.model.AnalyticsLogData;

import java.util.Map;

/**
 * @author okasurya on 5/16/18.
 */
public class NoOpLogger implements AnalyticsLogger {
    @Override
    public void save(String name, Map<String, Object> data) {

    }

    @Override
    public void wipe() {

    }
}
