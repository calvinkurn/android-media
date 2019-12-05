package com.tokopedia.home.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.multidex.MultiDexApplication;

import com.google.android.play.core.splitcompat.SplitCompat;
import com.google.firebase.FirebaseApp;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.analytics.container.GTMAnalytics;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.track.TrackApp;

import okhttp3.Response;

public class CustomApp extends BaseMainApplication implements TkpdCoreRouter, NetworkRouter {

    @Override
    public void onCreate() {
        SplitCompat.install(this);
        FirebaseApp.initializeApp(this);
        TrackApp.initTrackApp(this);
        TrackApp.getInstance().registerImplementation(TrackApp.GTM, GTMAnalytics.class);
        TrackApp.getInstance().initializeAllApis();
        GraphqlClient.init(this);
        GlobalConfig.DEBUG = true;
        com.tokopedia.abstraction.common.utils.GlobalConfig.DEBUG = true;
        super.onCreate();
    }

    @Override
    public Class<?> getDeeplinkClass() {
        return null;
    }

    @Override
    public Intent getSellerHomeActivityReal(Context context) {
        return null;
    }

    @Override
    public Intent getInboxTalkCallingIntent(Context mContext) {
        return null;
    }

    @Override
    public IAppNotificationReceiver getAppNotificationReceiver() {
        return null;
    }

    @Override
    public Class<?> getInboxMessageActivityClass() {
        return null;
    }

    @Override
    public Class<?> getInboxResCenterActivityClassReal() {
        return null;
    }

    @Override
    public Intent getActivitySellingTransactionShippingStatusReal(Context mContext) {
        return null;
    }

    @Override
    public Class getSellingActivityClassReal() {
        return null;
    }

    @Override
    public Intent getActivitySellingTransactionListReal(Context mContext) {
        return null;
    }

    @Override
    public Intent getHomeIntent(Context context) {
        return null;
    }

    @Override
    public Class<?> getHomeClass(Context context) throws ClassNotFoundException {
        return null;
    }

    @Override
    public NotificationPass setNotificationPass(Context mContext, NotificationPass mNotificationPass, Bundle data, String notifTitle) {
        return null;
    }

    @Override
    public Intent getInboxMessageIntent(Context mContext) {
        return null;
    }

    @Override
    public void onAppsFlyerInit() {

    }

    @Override
    public SessionHandler legacySessionHandler() {
        return new SessionHandler(this) {
            @Override
            public String getLoginName() {
                return "null";
            }

            @Override
            public String getGTMLoginID() {
                return "null";
            }

            @Override
            public String getShopID() {
                return "null";
            }

            @Override
            public String getLoginID() {
                return "null";
            }

            @Override
            public boolean isUserHasShop() {
                return false;
            }

            @Override
            public boolean isV4Login() {
                return false;
            }

            @Override
            public String getPhoneNumber() {
                return "null";
            }

            @Override
            public String getEmail() {
                return "null";
            }

            @Override
            public String getRefreshToken() {
                return "null";
            }

            @Override
            public String getAccessToken() {
                return "null";
            }

            @Override
            public String getFreshToken() {
                return null;
            }

            @Override
            public String getUserId() {
                return "null";
            }

            @Override
            public String getDeviceId() {
                return "null";
            }

            @Override
            public String getProfilePicture() {
                return "null";
            }

            @Override
            public boolean isMsisdnVerified() {
                return false;
            }

            @Override
            public boolean isHasPassword() {
                return false;
            }
        };
    }

    @Override
    public GCMHandler legacyGCMHandler() {
        return new GCMHandler(this);
    }

    @Override
    public void refreshFCMTokenFromBackgroundToCM(String token, boolean force) {

    }

    @Override
    public void refreshFCMFromInstantIdService(String token) {

    }

    @Override
    public void refreshFCMTokenFromForegroundToCM() {

    }

    @Override
    public void onForceLogout(Activity activity) {

    }

    @Override
    public void showTimezoneErrorSnackbar() {

    }

    @Override
    public void showMaintenancePage() {

    }

    @Override
    public void sendForceLogoutAnalytics(Response response, boolean isInvalidToken, boolean isRequestDenied) {

    }

    @Override
    public void showForceLogoutTokenDialog(String response) {

    }

    @Override
    public void showServerError(Response response) {

    }

    @Override
    public void logInvalidGrant(Response response) {

    }

    @Override
    public FingerprintModel getFingerprintModel() {
        return new FingerprintModel();
    }

    @Override
    public void doRelogin(String newAccessToken) {

    }
}
