package com.tokopedia.kol;

import android.app.Activity;

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
}
