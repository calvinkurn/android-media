package com.tokopedia.core.app;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;

import com.tokopedia.core.receiver.CartBadgeNotificationReceiver;
import com.tokopedia.core2.R;
import com.tokopedia.sellerhomedrawer.presentation.view.BaseSellerReceiverDrawerActivity;

/**
 * Created by Nisie on 31/08/15.
 */

/**
 * Extends one of BaseActivity from tkpd abstraction eg:BaseSimpleActivity, BaseStepperActivity, BaseTabActivity, etc
 */
@Deprecated
public abstract class TkpdActivity extends BaseSellerReceiverDrawerActivity implements
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
    protected int getLayoutRes() {
        return R.layout.sh_drawer_activity;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    protected void RefreshDrawer() {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
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

    public abstract int getDrawerPosition();

    @Override
    protected int setDrawerPosition() {
        return getDrawerPosition();
    }
}
