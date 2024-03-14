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

    void sendForceLogoutAnalytics(String url, boolean isInvalidToken,
                                  boolean isRequestDenied);

    void sendRefreshTokenAnalytics(String errorMessage);

    void showForceLogoutTokenDialog(String path);

    void showServerError(Response response);

    void logInvalidGrant(Response response);

    void logRefreshTokenException(String error, String type, String path, String accessToken);

    FingerprintModel getFingerprintModel();

    void doRelogin(String newAccessToken);

    void sendAnalyticsAnomalyResponse(String title,
                                      String accessToken, String refreshToken,
                                      String response, String request);
}
