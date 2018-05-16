package com.tokopedia.analytics.debugger;

import com.tokopedia.analytics.debugger.domain.model.AnalyticsLogData;

/**
 * @author okasurya on 5/16/18.
 */
public class NoOpLogger implements AnalyticsLogger {
    @Override
    public void save(AnalyticsLogData data) {

    }

    @Override
    public void wipe() {

    }
}
