package com.tokopedia.core.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerDeposit;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTopPoints;
import com.tokopedia.core.drawer2.domain.datamanager.DrawerDataManager;
import com.tokopedia.core.drawer2.domain.datamanager.DrawerDataManagerImpl;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;

import rx.subscriptions.CompositeSubscription;

/**
 * Created on 3/23/16.
 */
public abstract class DrawerPresenterActivity<T> extends BasePresenterActivity
        implements NotificationReceivedListener, DrawerDataListener {

    private static final String TAG = DrawerPresenterActivity.class.getSimpleName();

    protected T presenter;
    private Boolean isLogin;
    private CompositeSubscription compositeSubscription;
    private DrawerHelper drawerHelper;
    private SessionHandler sessionHandler;
    private DrawerDataManager drawerDataManager;
    private LocalCacheHandler drawerCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
        sessionHandler = new SessionHandler(this);
        drawerCache = new LocalCacheHandler(this, DrawerHelper.DRAWER_CACHE);
        setupDrawer();
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
            drawerHelper = ((TkpdCoreRouter) getApplication()).getDrawer(this, sessionHandler, drawerCache);
            drawerHelper.initDrawer(this);
            drawerHelper.setEnabled(true);
            drawerHelper.setSelectedPosition(setDrawerPosition());
            drawerDataManager = new DrawerDataManagerImpl(this, this);
            getDrawerProfile();
            getDrawerDeposit();
            getDrawerTopPoints();
            getDrawerTokoCash();
            getDrawerNotification();

        }
    }

    protected void getDrawerDeposit() {
        drawerDataManager.getDeposit();
    }

    protected void getDrawerTopPoints() {
        drawerDataManager.getTopPoints();
    }

    protected void getDrawerProfile() {
        drawerDataManager.getProfile();
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

    public void setDrawerEnabled(boolean isEnabled) {
        drawerHelper.setEnabled(isEnabled);
    }

    private void getDrawerNotification() {
        drawerDataManager.getNotification();
    }


    protected void getDrawerTokoCash() {
        drawerDataManager.getTokoCash();
    }

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
        drawerHelper.getAdapter().getData().clear();
        drawerHelper.getAdapter().setData(drawerHelper.createDrawerData());
        drawerHelper.setExpand();

    }

    @Override
    public void onErrorGetNotificationDrawer(String errorMessage) {

    }

    @Override
    public void onGetTokoCash(DrawerTokoCash tokoCash) {
        drawerHelper.getAdapter().getHeader().getData().setDrawerTokoCash(tokoCash);
        drawerHelper.getAdapter().getHeader().notifyDataSetChanged();
    }

    @Override
    public void onGetTopPoints(DrawerTopPoints topPoints) {
        drawerHelper.getAdapter().getHeader().getData().setDrawerTopPoints(topPoints);
        drawerHelper.getAdapter().getHeader().notifyDataSetChanged();
    }

    @Override
    public void onGetProfile(DrawerProfile profile) {
        drawerHelper.getAdapter().getHeader().getData().setDrawerProfile(profile);
        drawerHelper.getAdapter().getHeader().notifyDataSetChanged();
        drawerHelper.setFooterData(profile);
    }
}
