package com.tokopedia.abstraction;

import android.app.Activity;

import com.tokopedia.abstraction.common.data.model.session.UserSession;

/**
 * Created by nathan on 10/16/17.
 */

public interface AbstractionRouter {

    void goToForceUpdate(Activity activity);

    void onForceLogout(Activity activity);

    void showTimezoneErrorSnackbar();

    void showMaintenancePage();

    void showForceLogoutDialog();

    void sendForceLogoutAnalytics(String url);

    void showServerErrorSnackbar();

    void sendErrorNetworkAnalytics(String url, int code);

    void refreshLogin();

    void refreshToken();

    UserSession getSession();
}
