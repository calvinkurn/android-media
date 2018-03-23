package com.tokopedia.sellerapp;

import android.support.test.espresso.IdlingResource;
import android.util.Log;

import rx.plugins.RxJavaPlugins;

/**
 * Created by pt2121 on 3/7/15.
 */
public class RxJavaIdlingResource implements IdlingResource, RxJavaExecutionBridge {

    private static final boolean isLogged = false;

    private ResourceCallback cb;

    private Integer idler = 0;

    public RxJavaIdlingResource() {
        try {
            RxJavaPlugins.getInstance().registerObservableExecutionHook(new RxJavaExecutionHook(this));
        } catch (Exception e) {
        }
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        synchronized (idler) {
            if (isLogged) {
                Log.e("LOG", "called isidlenow: " + idler, null);
            }
            return idler == 0;
        }
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        if (isLogged) {
            Log.e("LOG", "called register Idle: " + idler, null);
        }
        this.cb = resourceCallback;
    }

    @Override
    public void onStart() {
        synchronized (idler) {
            idler++;
            if (isLogged) {
                Log.e("LOG", "called onstart: " + idler, null);
            }
        }
    }

    @Override
    public void onError() {
        synchronized (idler) {
            idler--;
            if (isLogged) {
                Log.e("LOG", "called onerrror: " + idler, null);
            }
            if (idler == 0 && cb != null) {
                cb.onTransitionToIdle();
            }
        }
    }

    @Override
    public void onEnd() {
        synchronized (idler) {
            idler--;
            if (isLogged) {
                Log.e("LOG", "called onend: " + idler, null);
            }
            if (idler == 0 && cb != null) {
                cb.onTransitionToIdle();
            }
        }
    }
}