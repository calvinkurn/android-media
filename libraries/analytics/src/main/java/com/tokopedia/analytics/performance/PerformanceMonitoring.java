package com.tokopedia.analytics.performance;

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
    private long pointStartTime;
    private long endTime;
    private Map<String, String> attributes = new HashMap<>();
    private Map<String, Long> metrics = new HashMap<>();

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
                this.traceName = traceName;
                if (trace != null) {
                    this.startTime = System.currentTimeMillis();
                    this.pointStartTime = System.currentTimeMillis();
                    trace.start();
                }
            }
        } catch (Exception ignored){ }
    }

    public void stopTrace() {
        if(trace != null){
            trace.stop();
            this.endTime = System.currentTimeMillis();
            if(FpmLogger.getInstance() != null) FpmLogger.getInstance().save(traceName, startTime, endTime, attributes, metrics);
        }
    }

    public void putMetric(String parameter, long value) {
        if (trace != null) {
            trace.putMetric(parameter, value);
            metrics.put(parameter, value);
        }
    }

    /**
     * use this to add point in your trace, to separate each process in a trace
     * @param tag
     */
    public void putPointMetric(String tag) {
        if (trace != null) {
            long currentTime = System.currentTimeMillis();
            long duration = currentTime - pointStartTime;
            pointStartTime = currentTime;

            trace.putMetric(tag, duration);
            metrics.put(tag, duration);
        }
    }


    public void putCustomAttribute(String attribute, String value) {
        if (trace != null) {
            trace.putAttribute(attribute, value);
            attributes.put(attribute, value);
        }
    }
}