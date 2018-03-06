package com.tokopedia.tkpdreactnative.react.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.tkpdreactnative.react.ReactConst;

/**
 * @author ricoharisin .
 */

public class ReactNativeActivity extends BaseActivity implements DefaultHardwareBackBtnHandler {

    public static final String USER_ID = "User_ID";
    protected ReactRootView reactRootView;
    protected ReactInstanceManager reactInstanceManager;

    public static Intent createReactNativeActivity(Context context, String reactScreenName, String userId) {
        Intent intent = new Intent(context, ReactNativeActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(USER_ID, userId);
        intent.putExtras(extras);
        return intent;
    }

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
        if(reactRootView != null) {
            reactRootView.unmountReactApplication();
        }   
        if (reactInstanceManager != null) {
            reactInstanceManager.onHostDestroy(this);
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (reactInstanceManager != null) {
            reactInstanceManager.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reactRootView = new ReactRootView(this);
        Bundle initialProps = getIntent().getExtras();
        reactInstanceManager = ((ReactApplication) getApplication()).getReactNativeHost().getReactInstanceManager();
        reactRootView.startReactApplication(reactInstanceManager, ReactConst.MAIN_MODULE, initialProps);
        setContentView(reactRootView);
    }

}
