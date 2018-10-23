package com.tokopedia.core.analytics.container;

import com.google.firebase.perf.metrics.Trace;

/**
 * Created by Herdi_WORK on 28.09.17.
 */

public interface IPerformanceMonitoring {


    Trace startTrace();

    void stopTrace();

    void incrementCounter(String counterName);

    void incrementCounter(String counterName, Long l);
}
