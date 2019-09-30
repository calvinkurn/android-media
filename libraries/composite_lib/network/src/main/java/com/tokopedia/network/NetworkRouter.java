package com.tokopedia.network;

import android.app.Activity;

import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.network.AbstractNetworkRouter;

import okhttp3.Response;

/**
 * @author ricoharisin .
 */

public interface NetworkRouter extends AbstractNetworkRouter{

    void onForceLogout(Activity activity);

    void showTimezoneErrorSnackbar();

    void showMaintenancePage();

    void sendForceLogoutAnalytics(Response response, boolean isInvalidToken,
                                  boolean isRequestDenied);

    void showForceLogoutTokenDialog(String response);

    void showServerError(Response response);

    void logInvalidGrant(Response response);

    FingerprintModel getFingerprintModel();

    void doRelogin(String newAccessToken);
}
