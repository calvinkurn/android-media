package com.tokopedia.core.app;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.localytics.android.Localytics;
import com.tokopedia.core.R;
import com.tokopedia.core.receiver.CartBadgeNotificationReceiver;

/**
 * Created by Nisie on 31/08/15.
 */
@Deprecated
public abstract class TkpdActivity extends DrawerPresenterActivity implements
        CartBadgeNotificationReceiver.ActionListener {

    private CartBadgeNotificationReceiver cartBadgeNotificationReceiver;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cartBadgeNotificationReceiver = new CartBadgeNotificationReceiver(this);
        IntentFilter intentFilter = new IntentFilter(CartBadgeNotificationReceiver.ACTION);
        registerReceiver(cartBadgeNotificationReceiver, intentFilter);
    }

    @Override
    protected int getContentId() {
        return R.layout.drawer_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDrawerData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    protected void RefreshDrawer() {
    }

    @Override
    protected void onPause() {
        MainApplication.setCurrentActivity(null);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Localytics.onNewIntent(this, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(cartBadgeNotificationReceiver);
    }

    @Override
    public void onRefreshBadgeCart() {
        invalidateOptionsMenu();
    }

    public void setDrawerEnabled(boolean isEnabled) {
        this.drawerHelper.setEnabled(isEnabled);
    }

    public abstract int getDrawerPosition();

    @Override
    protected int setDrawerPosition() {
        return getDrawerPosition();
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void setViewListener() {

    }
}
