package com.tokopedia.tradein_common.viewmodel;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

public class BaseLifeCycleObserver implements LifecycleObserver {

    private BaseViewModel baseViewModel;

    public BaseLifeCycleObserver(BaseViewModel viewModel) {
        this.baseViewModel = viewModel;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        baseViewModel.doOnCreate();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart() {
        baseViewModel.doOnStart();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop() {
        baseViewModel.doOnStop();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() {
        baseViewModel.doOnResume();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        baseViewModel.doOnPause();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        baseViewModel.doOnDestroy();
    }
}
