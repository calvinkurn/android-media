package com.tokopedia.sellerapp.anr;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public class AnrActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {
    static final AnrSupervisor sSupervisor = new AnrSupervisor();

    @Override
    public void onActivityCreated(@NonNull @NotNull Activity activity, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull @NotNull Activity activity) {
        sSupervisor.start();
    }

    @Override
    public void onActivityResumed(@NonNull @NotNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull @NotNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull @NotNull Activity activity) {
        sSupervisor.stop();
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull @NotNull Activity activity, @NonNull @NotNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull @NotNull Activity activity) {

    }
}
