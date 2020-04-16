package com.tokopedia.abstraction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;

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

    void showServerError(Response response);

    void gcmUpdate() throws IOException;

    void refreshToken() throws IOException;

    /**
     * Use PersistentCacheManager library
     * @return
     */
    @Deprecated
    CacheManager getGlobalCacheManager();

    void logInvalidGrant(Response response);

    boolean isAllowLogOnChuckInterceptorNotification();

    void onNewIntent(Context context, Intent intent);

    void onForceLogoutAnomaly(Activity activity);
}
