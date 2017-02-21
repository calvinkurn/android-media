package com.tokopedia.core.app;

import android.content.Intent;
import android.util.Log;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;

/**
 * Created on 3/23/16.
 */
public abstract class DrawerPresenterActivity<T> extends BasePresenterActivity
        implements NotificationReceivedListener {

    private static final String TAG = DrawerPresenterActivity.class.getSimpleName();

    protected T presenter;
    private Boolean isLogin;

    @Override
    protected void initView() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        toolbar.createToolbarWithDrawer();
        drawer.setEnabled(true);
        drawer.setDrawerPosition(setDrawerPosition());
    }

    protected abstract int setDrawerPosition();

    @Override
    protected void onStart() {
        super.onStart();
        isLogin = SessionHandler.isV4Login(this);
    }

    public boolean isLogin() {
        return isLogin;
    }

    @SuppressWarnings("unused")
    protected void RefreshDrawer() {
        drawer.updateData();
    }

    @Override
    protected void onPause() {
        MainApplication.setCurrentActivity(null);
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "on resume");
        super.onResume();
    }

    @Override
    public void onRefreshCart(int status) {
        LocalCacheHandler Cache = new LocalCacheHandler(this, TkpdCache.NOTIFICATION_DATA);
        Cache.putInt(TkpdCache.Key.IS_HAS_CART, status);
        Cache.applyEditor();
        invalidateOptionsMenu();
        MainApplication.resetCartStatus(false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onGetNotif() {

    }

    @Override
    public void onBackPressed() {
        if(drawer.isOpened()){
            drawer.closeDrawer();
        }else {
            super.onBackPressed();
        }
    }

    public void setDrawerEnabled(boolean isEnabled) {
        this.drawer.setEnabled(isEnabled);
    }
}
