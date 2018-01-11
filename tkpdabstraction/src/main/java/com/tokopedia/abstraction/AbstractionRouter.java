package com.tokopedia.abstraction;

import android.app.Activity;

import com.tokopedia.abstraction.common.data.model.analytic.Tracker;
import com.tokopedia.abstraction.common.data.model.session.UserSession;

import okhttp3.Response;

/**
 * Created by nathan on 10/16/17.
 */

public interface AbstractionRouter {

    void onForceLogout(Activity activity);

    void showTimezoneErrorSnackbar();

    void showMaintenancePage();

    void showForceLogoutDialog();

    void showServerError(Response response);

    void refreshLogin();

    void refreshToken();

    UserSession getSession();

    Tracker getTracker();
}
