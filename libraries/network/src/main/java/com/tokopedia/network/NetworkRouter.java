package com.tokopedia.network;

import android.app.Activity;

import com.tokopedia.network.data.model.FingerprintModel;

import okhttp3.Response;

/**
 * @author ricoharisin .
 */

public interface NetworkRouter {

    void onForceLogout(Activity activity);

    void showTimezoneErrorSnackbar();

    void showMaintenancePage();

    void showForceLogoutDialog(Response response);

    void showServerError(Response response);

    void showForceHockeyAppDialog();

    void logInvalidGrant(Response response);

    FingerprintModel getFingerprintModel();

    void doRelogin(String newAccessToken);

}
