package com.tokopedia.promogamification.common;

import android.content.Context;
import android.content.Intent;

import okhttp3.Interceptor;

/**
 * Created by nabillasabbaha on 3/29/18.
 */

public interface GamificationRouter {

    boolean isSupportedDelegateDeepLink(String appLinks);

    void goToHome(Context context);

    Intent getLoginIntent();
}
