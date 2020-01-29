package com.tokopedia.analytics.performance;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

import timber.log.Timber;

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
        try {
            FirebasePerformance fp = FirebasePerformance.getInstance();
            if (fp != null) {
                trace = fp.newTrace(traceName);
                if (trace != null) {
                    trace.start();
                }
            } else {
                Timber.e("P2#FIREBASE_PERFORMANCE_INIT_DF#%s", "Firebase Performance initialization failed");
            }
        } catch (Exception e){
            Timber.e(e, "P2#FIREBASE_PERFORMANCE_INIT_DF#%s", e.getMessage());
        }
    }

    public void stopTrace() {
        if(trace != null){
            trace.stop();
        }
    }

    public void putMetric(String parameter, long value) {
        if (trace != null) {
            trace.putMetric(parameter, value);
        }
    }

    public void putCustomAttribute(String attribute, String value) {
        if (trace != null) {
            trace.putAttribute(attribute, value);
        }
    }
}