package com.google.firebase.perf;

import android.content.Context;
import com.google.firebase.perf.metrics.Trace;
import androidx.annotation.NonNull;

import java.util.Map;


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
