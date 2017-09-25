package com.tokopedia.posapp.react;

import android.app.Application;

import com.facebook.react.ReactNativeHost;
import com.tokopedia.posapp.react.host.PosReactNativeHostDev;
import com.tokopedia.posapp.react.host.PosReactNativeHostLive;

/**
 * Created by okasurya on 8/29/17.
 */

public class PosReactNativeHostFactory {
    private static PosReactNativeHostFactory instance;

    private PosReactNativeHostFactory() {
    }

    public static ReactNativeHost init(Application application) {
        if (instance == null) instance = new PosReactNativeHostFactory();

        return instance.createReactNativeHostDev(application);
    }

    private ReactNativeHost createReactNativeHost(Application application) {
        return new PosReactNativeHostLive(application);
    }

    private ReactNativeHost createReactNativeHostDev(Application application) {
        return new PosReactNativeHostDev(application);
    }
}
