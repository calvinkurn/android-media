package com.tokopedia.core.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.drawer.receiver.TokoCashBroadcastReceiver;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerDeposit;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTopPoints;
import com.tokopedia.core.drawer2.di.DrawerInjector;
import com.tokopedia.core.drawer2.domain.datamanager.DrawerDataManager;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.databinder.DrawerHeaderDataBinder;
import com.tokopedia.core.drawer2.view.databinder.DrawerSellerHeaderDataBinder;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created on 3/23/16.
 */
public abstract class DrawerPresenterActivity<T> extends BasePresenterActivity
        implements NotificationReceivedListener, DrawerDataListener {

    private static final String TAG = DrawerPresenterActivity.class.getSimpleName();
    private static final int MAX_NOTIF = 999;

    protected T presenter;
    private Boolean isLogin;
    protected DrawerHelper drawerHelper;
    protected SessionHandler sessionHandler;
    protected DrawerDataManager drawerDataManager;
    protected LocalCacheHandler drawerCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionHandler = new SessionHandler(this);
        drawerCache = new LocalCacheHandler(this, DrawerHelper.DRAWER_CACHE);
        setupDrawer();
    }

    @Override
    protected int getContentId() {
        return R.layout.drawer_activity;
    }

    @Override
    protected void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.removeAllViews();
        initNotificationMenu(toolbar);
        initTitle(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    private void initTitle(Toolbar toolbar) {
        View title = getLayoutInflater().inflate(R.layout.custom_action_bar_title, null);
        TextView titleTextView = (TextView) title.findViewById(R.id.actionbar_title);
        titleTextView.setText(getTitle());
        toolbar.addView(title);
    }

    private void initNotificationMenu(Toolbar toolbar) {
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
        toolbar.addView(notif);
        toolbar.setNavigationIcon(null);
    }

    protected void setupDrawer() {
        drawerHelper = DrawerInjector.getDrawerHelper(this, sessionHandler, drawerCache);
        drawerHelper.initDrawer(this);
        drawerHelper.setEnabled(true);
        drawerHelper.setSelectedPosition(setDrawerPosition());
        drawerDataManager = DrawerInjector.getDrawerDataManager(this, this, sessionHandler, drawerCache);
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

    protected void getDrawerUserAttrUseCase(SessionHandler sessionHandler){
        drawerDataManager.getUserAttributes(sessionHandler);
    }

    protected void getProfileCompletion() {
        drawerDataManager.getProfileCompletion();
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
        super.onResume();
        updateDrawerData();
    }

    protected void updateDrawerData() {
        if (sessionHandler.isV4Login()) {
            getDrawerProfile();
            getDrawerDeposit();
            getDrawerNotification();

            if (!GlobalConfig.isSellerApp()) {
                getDrawerTopPoints();
                getDrawerTokoCash();
                getProfileCompletion();
                getDrawerUserAttrUseCase(sessionHandler);
            }
        }
    }

    @Override
    public void onRefreshCart(int status) {
        LocalCacheHandler Cache = new LocalCacheHandler(this, DrawerHelper.DRAWER_CACHE);
        Cache.putInt(DrawerNotification.IS_HAS_CART, status);
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
    public void onGetNotif(Bundle data) {

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
        if (drawerHelper.getAdapter().getHeader() instanceof DrawerHeaderDataBinder)
            ((DrawerHeaderDataBinder) drawerHelper.getAdapter().getHeader())
                    .getData().setDrawerDeposit(drawerDeposit);
        else if (drawerHelper.getAdapter().getHeader() instanceof DrawerSellerHeaderDataBinder)
            ((DrawerSellerHeaderDataBinder) drawerHelper.getAdapter()
                    .getHeader()).getData().setDrawerDeposit(drawerDeposit);
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
                String totalNotif = notification.getTotalNotif() > MAX_NOTIF ?
                        getString(R.string.max_notif) : String.valueOf(notification.getTotalNotif());
                notifRed.setText(totalNotif);
            }
        }
        if (notification.isUnread()) {
            MethodChecker.setBackground(notifRed, getResources().getDrawable(R.drawable.green_circle));
        } else {
            MethodChecker.setBackground(notifRed, getResources().getDrawable(R.drawable.red_circle));
        }

        setDataDrawer();
    }

    @Override
    public void onErrorGetNotificationTopchat(String errorMessage) {

    }

    @Override
    public void onSuccessGetTopChatNotification(int notifUnreads) {
        setDataDrawer();
    }

    private void setDataDrawer() {
        drawerHelper.getAdapter().getData().clear();
        drawerHelper.getAdapter().setData(drawerHelper.createDrawerData());
        drawerHelper.setExpand();
    }

    @Override
    public void onErrorGetNotificationDrawer(String errorMessage) {
        setDataDrawer();
    }


    @Override
    public void onGetTokoCash(DrawerTokoCash tokoCash) {
        if (drawerHelper.getAdapter().getHeader() instanceof DrawerHeaderDataBinder)
            ((DrawerHeaderDataBinder) drawerHelper.getAdapter().getHeader())
                    .getData().setDrawerTokoCash(tokoCash);
        else if (drawerHelper.getAdapter().getHeader() instanceof DrawerSellerHeaderDataBinder)
            ((DrawerSellerHeaderDataBinder) drawerHelper.getAdapter().getHeader())
                    .getData().setDrawerTokoCash(tokoCash);
        Intent intent = new Intent(TokoCashBroadcastReceiver.ACTION_GET_TOKOCASH);
        intent.putExtra(TokoCashBroadcastReceiver.EXTRA_RESULT_TOKOCASH_DATA,
                tokoCash);
        drawerHelper.getAdapter().getHeader().notifyDataSetChanged();
        sendBroadcast(intent);
    }

    @Override
    public void onErrorGetTokoCash(String errorMessage) {

    }

    @Override
    public void onGetTopPoints(DrawerTopPoints topPoints) {
        if (drawerHelper.getAdapter().getHeader() instanceof DrawerHeaderDataBinder)
            ((DrawerHeaderDataBinder) drawerHelper.getAdapter().getHeader())
                    .getData().setDrawerTopPoints(topPoints);
        else if (drawerHelper.getAdapter().getHeader() instanceof DrawerSellerHeaderDataBinder)
            ((DrawerSellerHeaderDataBinder) drawerHelper.getAdapter().getHeader())
                    .getData().setDrawerTopPoints(topPoints);
        drawerHelper.getAdapter().getHeader().notifyDataSetChanged();
    }

    @Override
    public void onErrorGetTopPoints(String errorMessage) {
    }

    @Override
    public void onGetProfile(DrawerProfile profile) {
        if (drawerHelper.getAdapter().getHeader() instanceof DrawerHeaderDataBinder)
            ((DrawerHeaderDataBinder) drawerHelper.getAdapter().getHeader())
                    .getData().setDrawerProfile(profile);
        else if (drawerHelper.getAdapter().getHeader() instanceof DrawerSellerHeaderDataBinder)
            ((DrawerSellerHeaderDataBinder) drawerHelper.getAdapter().getHeader())
                    .getData().setDrawerProfile(profile);
        drawerHelper.getAdapter().getHeader().notifyDataSetChanged();
        drawerHelper.setFooterData(profile);
    }

    @Override
    public void onErrorGetProfile(String errorMessage) {
    }

    @Override
    public void onErrorGetProfileCompletion(String errorMessage) {
    }

    @Override
    public void onSuccessGetProfileCompletion(int completion) {
        if (drawerHelper.getAdapter().getHeader() instanceof DrawerHeaderDataBinder)
            ((DrawerHeaderDataBinder) drawerHelper.getAdapter().getHeader())
                    .getData().setProfileCompletion(completion);
        else if (drawerHelper.getAdapter().getHeader() instanceof DrawerSellerHeaderDataBinder)
            ((DrawerSellerHeaderDataBinder) drawerHelper.getAdapter().getHeader())
                    .getData().setProfileCompletion(completion);
        drawerHelper.getAdapter().getHeader().notifyDataSetChanged();
    }

    @Override
    public Activity getActivity() {
        return this;
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
        drawerDataManager.unsubscribe();
    }


}
