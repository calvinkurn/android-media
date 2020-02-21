package com.tokopedia.sellerhomedrawer.presentation.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tokopedia.sellerhomedrawer.domain.service.SellerDrawerGetNotificationService;
import com.tokopedia.sellerhomedrawer.presentation.view.drawer.SellerDrawerPresenterActivity;

public class BaseSellerReceiverDrawerActivity extends SellerDrawerPresenterActivity {

    private BroadcastReceiver drawerGetNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals(SellerDrawerGetNotificationService.BROADCAST_GET_NOTIFICATION) && intent.getBooleanExtra(SellerDrawerGetNotificationService.GET_NOTIFICATION_SUCCESS, false)) {
                    updateDrawerData();
                } else if (intent.getAction().equals(SellerDrawerGetNotificationService.UPDATE_NOTIFICATON_DATA)) {
                    startDrawerGetNotificationService();
                }
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int setDrawerPosition() {
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcastReceiver();
        startDrawerGetNotificationService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterBroadcastReceiver();
    }

    @Nullable
    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    protected void startDrawerGetNotificationService() {
        SellerDrawerGetNotificationService.startService(this, true);
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SellerDrawerGetNotificationService.BROADCAST_GET_NOTIFICATION);
        LocalBroadcastManager.getInstance(this).registerReceiver(drawerGetNotificationReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(drawerGetNotificationReceiver);
    }
}
