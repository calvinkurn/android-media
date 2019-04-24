package com.tokopedia.kol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import okhttp3.Interceptor;

/**
 * @author by nisie on 2/6/18.
 */

public interface KolRouter {
    void actionApplinkFromActivity(Activity activity, String linkUrl);

    void openRedirectUrl(Activity activity, String url);

    Interceptor getChuckInterceptor();

    Intent getTopProfileIntent(Context context, String userId);

    Intent getLoginIntent(Context context);

    void shareFeed(Activity activity, String valueOf, String url, String title, String imageUrl,
                   String description);
}
