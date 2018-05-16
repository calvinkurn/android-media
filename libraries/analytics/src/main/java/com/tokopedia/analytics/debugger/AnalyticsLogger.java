package com.tokopedia.analytics.debugger;

import com.tokopedia.analytics.debugger.domain.model.AnalyticsLogData;

/**
 * @author okasurya on 5/16/18.
 */
public interface AnalyticsLogger {
    void save(AnalyticsLogData data);

    void wipe();
}
