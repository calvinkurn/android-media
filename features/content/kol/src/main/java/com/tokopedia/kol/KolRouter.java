package com.tokopedia.kol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import okhttp3.Interceptor;

/**
 * @author by nisie on 2/6/18.
 */

public interface KolRouter {
    String getKolCommentArgsPosition();

    String getKolCommentArgsTotalComment();

    void actionApplinkFromActivity(Activity activity, String linkUrl);

    void openRedirectUrl(Activity activity, String url);

    Interceptor getChuckInterceptor();

    Intent getTopProfileIntent(Context context, String userId);

    Intent getLoginIntent(Context context);
}
