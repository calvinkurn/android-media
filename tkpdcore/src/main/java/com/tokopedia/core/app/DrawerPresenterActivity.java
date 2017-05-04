package com.tokopedia.core.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer2.DrawerHelper;
import com.tokopedia.core.drawer2.datamanager.DrawerDataManager;
import com.tokopedia.core.drawer2.datamanager.DrawerDataManagerImpl;
import com.tokopedia.core.drawer2.viewmodel.DrawerDeposit;
import com.tokopedia.core.drawer2.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.viewmodel.DrawerTopPoints;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created on 3/23/16.
 */
public abstract class DrawerPresenterActivity<T> extends BasePresenterActivity
        implements NotificationReceivedListener {

    private static final String TAG = DrawerPresenterActivity.class.getSimpleName();

    protected T presenter;
    private Boolean isLogin;
    private CompositeSubscription compositeSubscription;
    private DrawerHelper drawerHelper;
    private SessionHandler sessionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
        sessionHandler = new SessionHandler(this);
        setupDrawer();
        setupToolbar();
    }

    protected void setupToolbar() {

    }

    protected void setupDrawer() {
        if (GlobalConfig.isSellerApp()) {
//            drawerHelper = new DrawerVariable(this);

        } else {
            drawerHelper = ((TkpdCoreRouter) getApplication()).getDrawer(this, sessionHandler);
            drawerHelper.initDrawer(this);
            drawerHelper.setEnabled(true);
            DrawerDataManager drawerDataManager = new DrawerDataManagerImpl(this);
            getDrawerProfile(drawerDataManager);
            getDrawerDeposit(drawerDataManager);
            getDrawerTopPoints(drawerDataManager);
            getDrawerTokoCash(drawerDataManager);
            getDrawerNotification(drawerDataManager);

        }
    }

    @Override
    protected void initView() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

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
//        if(drawer.isOpened()){
//            drawer.closeDrawer();
//        }else {
//            super.onBackPressed();
//        }
    }

    public void setDrawerEnabled(boolean isEnabled) {
//        this.drawer.setEnabled(isEnabled);
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

                                drawerHelper.getAdapter().notifyDataSetChanged();

                            }


                        }
                ));
    }


    private void getDrawerTokoCash(DrawerDataManager drawerDataManager) {
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
}
