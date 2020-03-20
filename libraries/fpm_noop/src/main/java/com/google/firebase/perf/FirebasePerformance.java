package com.google.firebase.perf;

import com.google.firebase.perf.metrics.Trace;
import androidx.annotation.NonNull;

public class FirebasePerformance {

    private static FirebasePerformance instance;

    public static FirebasePerformance getInstance() {
        if (instance == null) {
            instance = new FirebasePerformance();
        }

        return instance;
    }

    public Trace newTrace(@NonNull String var1) {
        return new Trace();
    }

}
