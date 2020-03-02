package com.tokopedia.analytics.performance;

import android.content.Context;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.tokopedia.analyticsdebugger.debugger.FpmLogger;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by meta on 14/11/18.
 */
public class PerformanceMonitoring {

    private Trace trace;
    private String traceName;
    private long startTime;
    private long endTime;
    private Map<String, String> attributes = new HashMap<>();
    private Map<String, Long> metrics = new HashMap<>();

    private Context context;

    public static PerformanceMonitoring start(Context context, String traceName) {
        PerformanceMonitoring performanceMonitoring = new PerformanceMonitoring();
        performanceMonitoring.startTrace(context, traceName);
        return performanceMonitoring;
    }

    public void startTrace(Context context, String traceName) {
        try {
            FirebasePerformance fp = FirebasePerformance.getInstance();
            if (fp != null) {
                this.context = context;
                trace = fp.newTrace(traceName);
                this.traceName = traceName;
                if (trace != null) {
                    this.startTime = System.currentTimeMillis();
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
            this.endTime = System.currentTimeMillis();
            if (context != null) {
                FpmLogger.getInstance(context).save(traceName, startTime, endTime, attributes, metrics);
            }
        }
    }

    public void putMetric(String parameter, long value) {
        if (trace != null) {
            trace.putMetric(parameter, value);
            metrics.put(parameter, value);
        }
    }

    public void putCustomAttribute(String attribute, String value) {
        if (trace != null) {
            trace.putAttribute(attribute, value);
            attributes.put(attribute, value);
        }
    }
}