package com.tokopedia.abstraction;

import android.app.Activity;
import android.view.MotionEvent;

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

    void init();

    void registerShake(String screenName,Activity activity);

    void unregisterShake();

    CacheManager getGlobalCacheManager();

    /**
     * To send analytic, use Track Library
     * val analytics = TrackApp.getInstance().getGTM()
     * analytics.push(event)
     * @return
     */
    @Deprecated
    AnalyticTracker getAnalyticTracker();

    void logInvalidGrant(Response response);

    void instabugCaptureUserStep(Activity activity, MotionEvent me);

    boolean isAllowLogOnChuckInterceptorNotification();
}
