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

    Intent getLoginIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);

    Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId);

    Intent getKolCommentActivity(Context context, int postId, int rowNumber);

    Intent getHomeIntent(Context context);

    void openRedirectUrl(Activity activity, String url);

    boolean isEnableInterestPick();

    void actionAppLink(Context context, String redirectUrl);

    Intent getBrandsWebViewIntent(Context context, String url);

    void shareFeed(Activity activity, String detailId, String url, String title, String imageUrl,
                   String description);

    Intent getAddToCartIntent(Context context, String productId, String price, String imageSource);

    void sendMoEngageOpenFeedEvent(boolean isEmptyFeed);
}
