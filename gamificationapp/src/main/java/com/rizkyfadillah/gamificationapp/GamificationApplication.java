package com.rizkyfadillah.gamificationapp;

import android.app.Activity;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.gamification.GamificationRouter;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by hendry on 02/04/18.
 */

public class GamificationApplication extends BaseMainApplication
        implements AbstractionRouter, GamificationRouter {
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
    public void showForceLogoutDialog(Response response) {

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
    public UserSession getSession() {
        return new UserSession() {
            @Override
            public String getAccessToken() {
                return null;
            }

            @Override
            public String getFreshToken() {
                return null;
            }

            @Override
            public String getUserId() {
                return null;
            }

            @Override
            public String getDeviceId() {
                return null;
            }

            @Override
            public boolean isLoggedIn() {
                return false;
            }

            @Override
            public String getShopId() {
                return null;
            }

            @Override
            public boolean hasShop() {
                return false;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getProfilePicture() {
                return null;
            }

            @Override
            public boolean isMsisdnVerified() {
                return false;
            }
        };
    }

    @Override
    public void init() {

    }

    @Override
    public void registerShake(String screenName) {

    }

    @Override
    public void unregisterShake() {

    }

    @Override
    public CacheManager getGlobalCacheManager() {
        return null;
    }

    @Override
    public AnalyticTracker getAnalyticTracker() {
        return null;
    }

    @Override
    public void showForceHockeyAppDialog() {

    }

    @Override
    public void logInvalidGrant(Response response) {

    }

    @Override
    public Interceptor getChuckInterceptor() {
        return new HttpLoggingInterceptor();
    }
}
