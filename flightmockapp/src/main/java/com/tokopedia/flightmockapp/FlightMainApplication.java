package com.tokopedia.flightmockapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.utils.GlobalConfig;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.TkpdFlight;
import com.tokopedia.flight.review.view.model.FlightCheckoutViewModel;

import java.io.IOException;

/**
 * Created by User on 10/24/2017.
 */

public class FlightMainApplication extends BaseMainApplication implements FlightModuleRouter, AbstractionRouter{

    @Override
    public void onCreate() {
        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        super.onCreate();
        TkpdFlight.init(getApplicationContext());
    }

    @Override
    public long getLongConfig(String flightAirport) {
        // use remote config from other module
        return 100;
    }

    @Override
    public void goToForceUpdate(Activity activity) {

    }

    @Override
    public void onForceLogout(Activity activity) {

    }

    @Override
    public void showTimezoneErrorSnackbar() {
        ServerErrorHandler.showTimezoneErrorSnackbar();
    }

    @Override
    public void showMaintenancePage() {
        ServerErrorHandler.showMaintenancePage();
    }

    @Override
    public void showForceLogoutDialog() {
        ServerErrorHandler.showMaintenancePage();
    }

    @Override
    public void sendForceLogoutAnalytics(String url) {
        ServerErrorHandler.sendForceLogoutAnalytics(url);
    }

    @Override
    public void showServerErrorSnackbar() {
        ServerErrorHandler.showServerErrorSnackbar();
    }

    @Override
    public void sendErrorNetworkAnalytics(String url, int code) {
        ServerErrorHandler.sendErrorNetworkAnalytics(url, code);
    }

    @Override
    public void refreshLogin() {
        SessionRefresh sessionRefresh = new SessionRefresh();
        try {
            sessionRefresh.refreshLogin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshToken() {
        AccessTokenRefresh accessTokenRefresh = new AccessTokenRefresh();
        try {
            accessTokenRefresh.refreshToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserSession getSession() {
        UserSession userSession = new UserSession() {
            @Override
            public String getAccessToken() {
                return "";
            }

            @Override
            public String getFreshToken() {
                return "";
            }

            @Override
            public String getUserId() {
                return "";
            }

            @Override
            public boolean isLoggedIn() {
                return true;
            }
        };
        return userSession;
    }

    @Override
    public Intent getLoginIntent() {
        return null;
    }

    @Override
    public void goToFlightActivity(Context context) {
        TkpdFlight.goToFlightActivity(context);
    }

    @Override
    public Intent getTopPayIntent(Activity activity, FlightCheckoutViewModel flightCheckoutViewModel) {
        return null;
    }

    @Override
    public int getTopPayPaymentSuccessCode() {
        return 0;
    }

    @Override
    public int getTopPayPaymentFailedCode() {
        return 0;
    }

    @Override
    public int getTopPayPaymentCancelCode() {
        return 0;
    }
}
