package com.tokopedia.gamification;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;

import okhttp3.Interceptor;

/**
 * Created by nabillasabbaha on 3/29/18.
 */

public interface GamificationRouter {

    Interceptor getChuckInterceptor();

    void actionApplink(Activity activity, String applink);

    Intent getWebviewActivityWithIntent(Context context, String url, String title);
}
