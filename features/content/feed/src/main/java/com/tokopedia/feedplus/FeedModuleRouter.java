package com.tokopedia.feedplus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import okhttp3.Interceptor;

/**
 * Created by meyta on 2/12/18.
 */

public interface FeedModuleRouter {

    Interceptor getChuckInterceptor();

    Intent getHomeIntent(Context context);

    void openRedirectUrl(Activity activity, String url);

    boolean isEnableInterestPick();

    void shareFeed(Activity activity, String detailId, String url, String title, String imageUrl,
                   String description);

    void sendMoEngageOpenFeedEvent(boolean isEmptyFeed);
}
