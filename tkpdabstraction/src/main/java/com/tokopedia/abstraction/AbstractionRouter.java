package com.tokopedia.abstraction;

import android.app.Activity;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by nathan on 10/16/17.
 */

public interface AbstractionRouter {

    void onForceLogout(Activity activity);

    void showTimezoneErrorSnackbar();

    void showMaintenancePage();

    void showForceLogoutDialog(Response response);

    void showServerError(Response response);

    void gcmUpdate() throws IOException;

    void refreshToken() throws IOException;

    UserSession getSession();

    CacheManager getGlobalCacheManager();

    AnalyticTracker getAnalyticTracker();

    void showForceHockeyAppDialog();

    void logInvalidGrant(Response response);
}
