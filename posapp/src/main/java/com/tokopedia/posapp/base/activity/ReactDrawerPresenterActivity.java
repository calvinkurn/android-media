package com.tokopedia.posapp.base.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.di.DrawerInjector;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created by okasurya on 9/26/17.
 */

public abstract class ReactDrawerPresenterActivity<T> extends DrawerPresenterActivity<T> {
    private ReactInstanceManager reactInstanceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDrawer();
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void initDrawer() {
        sessionHandler = new SessionHandler(this);
        drawerCache = new LocalCacheHandler(this, DrawerHelper.DRAWER_CACHE);
        drawerHelper = DrawerInjector.getDrawerHelper(this, sessionHandler, drawerCache);
        drawerHelper.initDrawer(this);
        drawerHelper.setEnabled(true);
        drawerHelper.setSelectedPosition(setDrawerPosition());
    }
}
