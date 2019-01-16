package com.tokopedia.core.analytics.container;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

/**
 * Created by Herdi_WORK on 28.09.17.
 */
@Deprecated
public class PerfMonContainer implements IPerformanceMonitoring {

    private static Trace trace;

    private PerfMonContainer(String traceName){
        trace = FirebasePerformance.getInstance().newTrace(traceName);
    }

    public static IPerformanceMonitoring initTraceInstance(String traceName) {
        return new PerfMonContainer(traceName);
    }

    @Override
    public Trace startTrace() {
        if(trace!=null){
            trace.start();
            return trace;
        }else{
            return null;
        }
    }

    @Override
    public void stopTrace() {
        if(trace!=null){
            trace.stop();
        }
    }

    @Override
    public void incrementCounter(String counterName) {
        if(trace!=null){
            incrementCounter(counterName,1L);
        }
    }

    @Override
    public void incrementCounter(String counterName, Long l) {
        if(trace!=null){
            trace.incrementCounter(counterName, l);
        }
    }
}
