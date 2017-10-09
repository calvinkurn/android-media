package com.tokopedia.posapp.base.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;

/**
 * Created by okasurya on 9/26/17.
 */

public abstract class ReactDrawerPresenterActivity<T> extends DrawerPresenterActivity<T> {
    private ReactInstanceManager reactInstanceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reactInstanceManager = ((ReactApplication) getApplication()).getReactNativeHost().getReactInstanceManager();
    }

    protected ReactInstanceManager getReactInstanceManager() {
        return reactInstanceManager;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && reactInstanceManager != null) {
            reactInstanceManager.showDevOptionsDialog();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onGetNotificationDrawer(DrawerNotification notification) {

    }

    @Override
    public void onGetNotif() {

    }
}
