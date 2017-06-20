package com.tokopedia.core.app;

import android.content.Context;
import android.os.Bundle;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.tokopedia.core.react.ReactSingleton;

/**
 * @author ricoharisin .
 */

public abstract class ReactNativeActivity extends BaseActivity implements DefaultHardwareBackBtnHandler {

    protected ReactRootView reactRootView;
    protected ReactInstanceManager reactInstanceManager;

    public abstract String getModuleName();

    @Override
    public void onPause() {
        super.onPause();

        if (reactInstanceManager != null) {
            reactInstanceManager.onHostPause(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (reactInstanceManager != null) {
            reactInstanceManager.onHostResume(this, this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (reactInstanceManager != null) {
            reactInstanceManager.onHostDestroy(this);
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reactRootView = new ReactRootView(this);
        reactInstanceManager = ReactSingleton.getReactInstanceManager();
        reactRootView.startReactApplication(reactInstanceManager, getModuleName(), null);
        setContentView(reactRootView);
    }

}
