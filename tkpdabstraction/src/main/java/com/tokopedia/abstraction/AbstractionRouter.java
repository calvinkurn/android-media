package com.tokopedia.abstraction;

import android.app.Activity;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;

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

    void sendErrorNetworkAnalytics(String url, int errorCode);

    void sendForceLogoutAnalytics(String url);

    UserSession getSession();

    CacheManager getGlobalCacheManager();

    AnalyticTracker getAnalyticTracker();
}
