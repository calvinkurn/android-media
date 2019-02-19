package com.tokopedia.analytics.performance;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

/**
 * Created by meta on 14/11/18.
 */
public class PerformanceMonitoring {

    private Trace trace;

    public static PerformanceMonitoring start(String traceName) {
        PerformanceMonitoring performanceMonitoring = new PerformanceMonitoring();
        performanceMonitoring.startTrace(traceName);
        return performanceMonitoring;
    }

    public void startTrace(String traceName) {
        trace = FirebasePerformance.getInstance().newTrace(traceName);
        trace.start();
    }

    public void stopTrace() {
        if(trace != null){
            trace.stop();
        }
    }

    public void incrementCounter(String counterName) {
        if(trace != null){
            incrementCounter(counterName,1L);
        }
    }

    public void incrementCounter(String counterName, Long l) {
        if(trace != null){
            trace.incrementCounter(counterName, l);
        }
    }
}
