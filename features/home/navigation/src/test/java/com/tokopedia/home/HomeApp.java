package com.tokopedia.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;

import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;

import org.robolectric.annotation.internal.Instrument;

import java.io.IOException;

import okhttp3.Response;

import static org.mockito.Mockito.mock;

@Instrument
public class HomeApp extends BaseMainApplication implements NetworkRouter, AbstractionRouter {
    @Override
    public void onCreate() {
        super.onCreate();
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
    public void sendForceLogoutAnalytics(String url, boolean isInvalidToken, boolean isRequestDenied) {

    }

    @Override
    public void sendRefreshTokenAnalytics(String errorMessage) {

    }

    @Override
    public void showForceLogoutTokenDialog(String response) {

    }

    @Override
    public void showServerError(Response response) {

    }

    @Override
    public void gcmUpdate() throws IOException {

    }

    @Override
    public void refreshToken() throws IOException {

    }

    @Override
    public CacheManager getPersistentCacheManager() {
        return mock(CacheManager.class);
    }

    @Override
    public void logInvalidGrant(Response response) {

    }

    @Override
    public boolean isAllowLogOnChuckInterceptorNotification() {
        return false;
    }

    @Override
    public void onNewIntent(Context context, Intent intent) {

    }

    @Override
    public FingerprintModel getFingerprintModel() {
        return mock(FingerprintModel.class);
    }

    @Override
    public void doRelogin(String newAccessToken) {

    }

    @Override
    public void sendAnalyticsAnomalyResponse(String s, String s1, String s2, String s3, String s4) {

    }
}
