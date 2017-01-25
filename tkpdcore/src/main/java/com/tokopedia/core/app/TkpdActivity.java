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
import com.tokopedia.core.drawer2.viewmodel.DrawerDeposit;
import com.tokopedia.core.drawer2.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.viewmodel.DrawerTopPoints;
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
            getDrawerProfile(drawerDataManager);
            getDrawerDeposit(drawerDataManager);
            getDrawerTopPoints(drawerDataManager);
            getDrawerTokoCash(drawerDataManager);
            getDrawerNotification(drawerDataManager);

        }
    }

    private void getDrawerNotification(DrawerDataManager drawerDataManager) {
        compositeSubscription.add(drawerDataManager.getNotification(this)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread(),true)
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(
                        new Subscriber<DrawerNotification>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                CommonUtils.dumper("NISNIS " + e.toString());
                            }

                            @Override
                            public void onNext(DrawerNotification notification) {

                                drawerHelper.setNotification(notification);
                                drawerHelper.getAdapter().notifyDataSetChanged();

                            }


                        }
                ));
    }


    private void getDrawerTokoCash(DrawerDataManager drawerDataManager) {
        SessionHandler sessionHandler = new SessionHandler(this);
        compositeSubscription.add(drawerDataManager.getTokoCash(sessionHandler.getAccessToken(this))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread(),true)
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(
                        new Subscriber<DrawerTokoCash>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                CommonUtils.dumper("NISNIS " + e.toString());
                            }

                            @Override
                            public void onNext(DrawerTokoCash tokoCash) {

                                drawerHelper.getAdapter().getHeader().getData().setDrawerTokoCash(tokoCash);
                                drawerHelper.getAdapter().getHeader().notifyDataSetChanged();

                            }


                        }
                ));
    }

    private void getDrawerTopPoints(DrawerDataManager drawerDataManager) {
        compositeSubscription.add(drawerDataManager.getTopPoints(this)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread(),true)
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(
                        new Subscriber<DrawerTopPoints>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                CommonUtils.dumper("NISNIS " + e.toString());
                            }

                            @Override
                            public void onNext(DrawerTopPoints topPoints) {

                                drawerHelper.getAdapter().getHeader().getData().setDrawerTopPoints(topPoints);
                                drawerHelper.getAdapter().getHeader().notifyDataSetChanged();

                            }


                        }
                ));
    }

    private void getDrawerDeposit(DrawerDataManager drawerDataManager) {
        compositeSubscription.add(drawerDataManager.getDeposit(this)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread(),true)
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(
                        new Subscriber<DrawerDeposit>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                CommonUtils.dumper("NISNIS " + e.toString());
                            }

                            @Override
                            public void onNext(DrawerDeposit deposit) {

                                drawerHelper.getAdapter().getHeader().getData().setDrawerDeposit(deposit);
                                drawerHelper.getAdapter().getHeader().notifyDataSetChanged();

                            }


                        }
                ));
    }

    private void getDrawerProfile(DrawerDataManager drawerDataManager) {
        compositeSubscription.add(drawerDataManager.getDrawerProfile(this)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread(),true)
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(
                        new Subscriber<DrawerProfile>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                CommonUtils.dumper("NISNIS " + e.toString());
                            }

                            @Override
                            public void onNext(DrawerProfile profile) {

                                drawerHelper.getAdapter().getHeader().getData().setDrawerProfile(profile);
                                drawerHelper.getAdapter().getHeader().notifyDataSetChanged();
                                drawerHelper.setFooterData(profile);
                            }


                        }
                ));
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
