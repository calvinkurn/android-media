package com.tokopedia.mitra.fingerprint;

import android.app.Activity;
import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.mitra.BuildConfig;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;

import okhttp3.Response;

/**
 * Created by Rizky on 30/08/18.
 */
public class MitraApplication extends BaseMainApplication implements NetworkRouter {

    private static MitraApplication instance;
    private LocationUtils locationUtils;

    @Override
    public void onCreate() {
        super.onCreate();

        initDbFlow();

        instance = this;

        locationUtils = new LocationUtils(this);
        locationUtils.initLocationBackground();
    }

    public static MitraApplication getInstance() {
        return instance;
    }

    protected void initDbFlow() {
        if(BuildConfig.DEBUG) {
            FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
        }
        FlowManager.init(new FlowConfig.Builder(this)
                .build());
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
    public void showForceLogoutDialog(Response response) {

    }

    @Override
    public void showForceLogoutTokenDialog(String response) {

    }

    @Override
    public void showServerError(Response response) {

    }

    @Override
    public void showForceHockeyAppDialog() {

    }

    @Override
    public void logInvalidGrant(Response response) {

    }

    @Override
    public FingerprintModel getFingerprintModel() {
        return FingerprintModelGenerator.generateFingerprintModel(this);
    }

    @Override
    public void doRelogin(String newAccessToken) {

    }

    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        locationUtils.deInitLocationBackground();
    }

}
