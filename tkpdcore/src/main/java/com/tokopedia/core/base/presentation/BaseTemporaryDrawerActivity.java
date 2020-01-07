package com.tokopedia.core.base.presentation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tokopedia.core.drawer2.service.DrawerGetNotificationService;
import com.tokopedia.core2.R;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.util.GlobalConfig;

/**
 * Created by meta on 23/07/18.
 */
public class BaseTemporaryDrawerActivity<T> extends DrawerPresenterActivity<T> {

    private BroadcastReceiver drawerGetNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == context || null == intent.getAction()) {
                return;
            }

            if (intent.getAction().equals(DrawerGetNotificationService.BROADCAST_GET_NOTIFICATION)
                    && intent.getBooleanExtra(DrawerGetNotificationService.GET_NOTIFICATION_SUCCESS, false)){
                updateDrawerData();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GlobalConfig.isSellerApp()) {
            registerBroadcastReceiver();
            startDrawerGetNotificationServiceOnResume();
        }
    }

    protected void startDrawerGetNotificationServiceOnResume(){
        DrawerGetNotificationService.startService(this,true,false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (GlobalConfig.isSellerApp())
            unregisterBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DrawerGetNotificationService.BROADCAST_GET_NOTIFICATION);
        LocalBroadcastManager.getInstance(this).registerReceiver(drawerGetNotificationReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver() {
        LocalBroadcastManager
                .getInstance(this)
                .unregisterReceiver(drawerGetNotificationReceiver);
    }

    @Override
    public void initialize() {
        if (GlobalConfig.isSellerApp()) {
            super.initialize();
        }
    }

    @Override
    protected int getContentId() {
        if (GlobalConfig.isSellerApp())
            return super.getContentId();
        return com.tokopedia.abstraction.R.layout.activity_base_legacy_light;
    }

    @Override
    protected void updateDrawerData() {
        if (GlobalConfig.isSellerApp())
            super.updateDrawerData();
    }

    @Override
    protected void setupToolbar() {
        if (GlobalConfig.isSellerApp())
            super.setupToolbar();
        else {
            toolbar = findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowCustomEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }

            if (isLightToolbarThemes()) {
                setLightToolbarStyle();
            }
        }
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return GlobalConfig.isCustomerApp();
    }

    @Override
    protected void initView() {
        if (GlobalConfig.isSellerApp())
            super.initView();
    }

    @Override
    protected int setDrawerPosition() {
        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setupURIPass(Uri data) { }

    @Override
    protected void setupBundlePass(Bundle extras) { }

    @Override
    protected void initialPresenter() { }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void setViewListener() { }

    @Override
    protected void initVar() { }

    @Override
    protected void setActionVar() { }

}
