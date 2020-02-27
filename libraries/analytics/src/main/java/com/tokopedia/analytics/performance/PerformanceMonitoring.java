package com.tokopedia.analytics.performance;

import android.content.Context;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.analytics.debugger.FpmLogger;

import timber.log.Timber;

/**
 * Created by meta on 14/11/18.
 */
public class PerformanceMonitoring {

    private Trace trace;
    private PerformanceLogModel performanceLogModel;
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
                performanceLogModel = new PerformanceLogModel(traceName);
                if (trace != null) {
                    performanceLogModel.setStartTime(System.currentTimeMillis());
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
            performanceLogModel.setEndTime(System.currentTimeMillis());
            FpmLogger.getInstance(context).save(performanceLogModel);
        }
    }

    public void putMetric(String parameter, long value) {
        if (trace != null) {
            trace.putMetric(parameter, value);
            performanceLogModel.putMetric(parameter, value);
        }
    }

    public void putCustomAttribute(String attribute, String value) {
        if (trace != null) {
            trace.putAttribute(attribute, value);
            performanceLogModel.putAtrribute(attribute, value);
        }
    }
}