package com.tokopedia.core.app;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.localytics.android.Localytics;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer.DrawerVariable;
import com.tokopedia.core.drawer2.DrawerHelper;
import com.tokopedia.core.drawer2.datamanager.DrawerDataManager;
import com.tokopedia.core.drawer2.datamanager.DrawerDataManagerImpl;
import com.tokopedia.core.drawer2.viewmodel.DrawerData;
import com.tokopedia.core.gcm.FCMMessagingService.NotificationListener;
import com.tokopedia.core.receiver.CartBadgeNotificationReceiver;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Nisie on 31/08/15.
 */
public abstract class TkpdActivity extends TActivity implements NotificationListener,
        CartBadgeNotificationReceiver.ActionListener {

    private Boolean isLogin;
    private CartBadgeNotificationReceiver cartBadgeNotificationReceiver;
    private DrawerHelper drawerHelper;
    private CompositeSubscription compositeSubscription;

    @Override
    public void onStart() {
        super.onStart();
        isLogin = SessionHandler.isV4Login(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        toolbar.createToolbarWithDrawer();
//        drawer.setEnabled(true);

        compositeSubscription = new CompositeSubscription();
        setupDrawer();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        cartBadgeNotificationReceiver = new CartBadgeNotificationReceiver(this);
        IntentFilter intentFilter = new IntentFilter(CartBadgeNotificationReceiver.ACTION);
        registerReceiver(cartBadgeNotificationReceiver, intentFilter);
    }

    protected void setupDrawer() {
        if (GlobalConfig.isSellerApp()) {
//            drawerHelper = new DrawerVariable(this);

        } else {
            drawerHelper = ((TkpdCoreRouter) getApplication()).getDrawer(this);
            drawerHelper.initDrawer(this);
            drawerHelper.setEnabled(true);
            DrawerDataManager drawerDataManager = new DrawerDataManagerImpl();
            compositeSubscription.add(drawerDataManager.getDrawerData(this)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .subscribe(
                            new Subscriber<DrawerData>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    CommonUtils.dumper("NISNIS " + e.toString());
                                }

                                @Override
                                public void onNext(DrawerData drawerData) {
                                    CommonUtils.dumper("NISNIS onNext " + drawerData.getDrawerProfile().getDeposit());

                                    drawerHelper.setData(drawerData);


                                }


                            }
                    ));
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetNotif() {

    }

    protected void RefreshDrawer() {
//        drawer.updateData();
    }

    @Override
    protected void onPause() {
        MainApplication.setCurrentActivity(null);
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TkpdActivity.class.getSimpleName(), "on resume");
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
        Localytics.onNewIntent(this, intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerHelper.isOpened()) {
            drawerHelper.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        drawer.unsubscribe();
        unregisterReceiver(cartBadgeNotificationReceiver);
    }

    @Override
    public void onRefreshBadgeCart() {
        invalidateOptionsMenu();
    }

    public void setDrawerEnabled(boolean isEnabled) {
        this.drawerHelper.setEnabled(isEnabled);
    }
}
