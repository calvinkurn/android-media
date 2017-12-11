package com.tokopedia.flightmockapp;

import android.app.Activity;
import android.content.Intent;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdFlightGeneratedDatabaseHolder;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.utils.GlobalConfig;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.common.di.component.DaggerFlightComponent;
import com.tokopedia.flight.common.di.component.FlightComponent;

import java.io.IOException;

/**
 * Created by User on 10/24/2017.
 */

public class FlightMainApplication extends BaseMainApplication implements FlightModuleRouter, AbstractionRouter{

    private FlightComponent flightComponent;

    @Override
    public void onCreate() {
        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        super.onCreate();
        initDBFlow();
    }

    private void initDBFlow() {
        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseHolder(TkpdFlightGeneratedDatabaseHolder.class)
                .build());
    }

    @Override
    public FlightComponent getFlightComponent() {
        if (flightComponent == null) {
            flightComponent = DaggerFlightComponent.builder().baseAppComponent(getBaseAppComponent()).build();
        }
        return flightComponent;
    }

    @Override
    public long getLongConfig(String flightAirport) {
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
                return SessionHandler.getAccessToken();
            }

            @Override
            public String getFreshToken() {
                return SessionHandler.getAccessToken();
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
}
