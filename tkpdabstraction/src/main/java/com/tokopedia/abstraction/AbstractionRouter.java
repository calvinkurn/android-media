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

    UserSession getSession();

    void init();

    void registerShake(String screenName);

    void unregisterShake();

    CacheManager getGlobalCacheManager();

    AnalyticTracker getAnalyticTracker();
}
