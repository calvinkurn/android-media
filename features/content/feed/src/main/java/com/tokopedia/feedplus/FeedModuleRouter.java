package com.tokopedia.feedplus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import okhttp3.Interceptor;

/**
 * Created by meyta on 2/12/18.
 */

public interface FeedModuleRouter {

    Interceptor getChuckInterceptor();

    Intent getLoginIntent(Context context);

    AnalyticTracker getAnalyticTracker();

    Intent getShopPageIntent(Context context, String shopId);

    Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId);

    Intent getKolCommentActivity(Context context, int postId, int rowNumber);

    Intent getHomeIntent(Context context);

    void goToProductDetail(Context context, String productId, String imageSourceSingle,
                           String name, String price);

    void openRedirectUrl(Activity activity, String url);
}
