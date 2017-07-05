package com.tokopedia.core.react;

import android.app.Application;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.shell.MainReactPackage;
import com.tokopedia.core.BuildConfig;

/**
 * @author ricoharisin .
 */

public class ReactSingleton {

    private static ReactInstanceManager reactInstanceManager;

    public static void init(Application application) {
        reactInstanceManager = ReactInstanceManager.builder()
                .setApplication(application)
                .setBundleAssetName("index.android.bundle")
                .setJSMainModuleName("reactscript/index.android")
                .addPackage(new MainReactPackage())
                .addPackage(new CoreReactPackage())
                .setUseDeveloperSupport(true)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();
    }

    public static ReactInstanceManager getReactInstanceManager() {
        return reactInstanceManager;
    }
}
