package com.tokopedia.core.app;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.localytics.android.Localytics;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.domain.datamanager.DrawerDataManager;
import com.tokopedia.core.drawer2.domain.datamanager.DrawerDataManagerImpl;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerDeposit;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTopPoints;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.receiver.CartBadgeNotificationReceiver;
import com.tokopedia.core.util.Drawer;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Nisie on 31/08/15.
 */
public abstract class TkpdActivity extends TActivity implements
        NotificationReceivedListener,
        CartBadgeNotificationReceiver.ActionListener,
        DrawerDataListener {

    private CartBadgeNotificationReceiver cartBadgeNotificationReceiver;
    protected DrawerHelper drawerHelper;
    protected SessionHandler sessionHandler;
    private CompositeSubscription compositeSubscription;
    private DrawerDataManager drawerDataManager;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
        sessionHandler = new SessionHandler(this);
        setupDrawer();

        cartBadgeNotificationReceiver = new CartBadgeNotificationReceiver(this);
        IntentFilter intentFilter = new IntentFilter(CartBadgeNotificationReceiver.ACTION);
        registerReceiver(cartBadgeNotificationReceiver, intentFilter);
    }

    @Override
    protected void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.removeAllViews();
        View notif = getLayoutInflater().inflate(
                R.layout.custom_actionbar_drawer_notification, null);
        final ImageView drawerToggle = (ImageView) notif.findViewById(R.id.toggle_but_ab);
        drawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerHelper.isOpened()) {
                    drawerHelper.closeDrawer();
                } else {
                    drawerHelper.openDrawer();
                }
            }
        });
        TextView notifRed = (TextView) notif.findViewById(R.id.toggle_count_notif);
        toolbar.addView(notif);

        View title = getLayoutInflater().inflate(R.layout.custom_action_bar_title, null);
        TextView titleTextView = (TextView) title.findViewById(R.id.actionbar_title);
        titleTextView.setText(getTitle());
        toolbar.addView(title);
        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
    }


    protected void setupDrawer() {
        if (GlobalConfig.isSellerApp()) {
//            drawerHelper = new DrawerVariable(this);

        } else {
            drawerHelper = ((TkpdCoreRouter) getApplication()).getDrawer(this, sessionHandler);
            drawerHelper.initDrawer(this);
            drawerHelper.setEnabled(true);
            drawerHelper.setSelectedPosition(getDrawerPosition());
            drawerDataManager = new DrawerDataManagerImpl(this, this);
            if (sessionHandler.isV4Login()) {
                getDrawerProfile(drawerDataManager);
                getDrawerDeposit();
                getDrawerTopPoints(drawerDataManager);
                getDrawerTokoCash();
                getDrawerNotification();
            }

        }
    }

    protected void getDrawerDeposit() {
        drawerDataManager.getDeposit();
    }

    protected void getDrawerNotification() {
        drawerDataManager.getNotification();
    }


    protected void getDrawerTokoCash() {

        drawerDataManager.getTokoCash();
//        compositeSubscription.add(drawerDataManager.getTokoCash(sessionHandler.getAccessToken(this))
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread(), true)
//                .unsubscribeOn(Schedulers.newThread())
//                .subscribe(
//                        new Subscriber<DrawerTokoCash>() {
//                            @Override
//                            public void onCompleted() {
//
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                CommonUtils.dumper("NISNIS " + e.toString());
//                            }
//
//                            @Override
//                            public void onNext(DrawerTokoCash tokoCash) {
//
//                                drawerHelper.getAdapter().getHeader().getData().setDrawerTokoCash(tokoCash);
//                                drawerHelper.getAdapter().getHeader().notifyDataSetChanged();
//
//                            }
//
//
//                        }
//                ));
    }

    protected void getDrawerTopPoints(DrawerDataManager drawerDataManager) {
        compositeSubscription.add(drawerDataManager.getTopPoints(this)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread(), true)
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

    protected void getDrawerProfile(DrawerDataManager drawerDataManager) {
        compositeSubscription.add(drawerDataManager.getDrawerProfile(this)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread(), true)
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
    }

    @Override
    protected void onPause() {
        MainApplication.setCurrentActivity(null);
        super.onPause();
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
        compositeSubscription.unsubscribe();
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
    public void onGetDeposit(DrawerDeposit drawerDeposit) {
        drawerHelper.getAdapter().getHeader().getData().setDrawerDeposit(drawerDeposit);
        drawerHelper.getAdapter().getHeader().notifyDataSetChanged();
    }

    @Override
    public void onErrorGetDeposit(String errorMessage) {

    }

    @Override
    public void onGetNotificationDrawer(DrawerNotification notification) {
        int notificationCount = notification.getTotalNotif();

        TextView notifRed = (TextView) toolbar.getRootView().findViewById(R.id.toggle_count_notif);
        if (notifRed != null) {
            if (notificationCount <= 0) {
                notifRed.setVisibility(View.GONE);
            } else {
                notifRed.setVisibility(View.VISIBLE);
                String totalNotif = notification.getTotalNotif() > 999 ? "999+" : String.valueOf(notification.getTotalNotif());
                notifRed.setText(totalNotif);
            }
        }
        if (notification.isUnread()) {
            MethodChecker.setBackground(notifRed, getResources().getDrawable(R.drawable.green_circle));
        } else {
            MethodChecker.setBackground(notifRed, getResources().getDrawable(R.drawable.red_circle));
        }
        drawerHelper.getAdapter().notifyDataSetChanged();

    }

    @Override
    public void onErrorGetNotificationDrawer(String errorMessage) {
    }

    @Override
    public void onGetTokoCash(DrawerTokoCash tokoCash) {
        drawerHelper.getAdapter().getHeader().getData().setDrawerTokoCash(tokoCash);
        drawerHelper.getAdapter().getHeader().notifyDataSetChanged();
    }
}
