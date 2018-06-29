package com.tokopedia.feedplus;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

/**
 * Created by meyta on 2/12/18.
 */

public interface FeedModuleRouter {

    Intent getLoginIntent(Context context);

    AnalyticTracker getAnalyticTracker();

    Intent getShopPageIntent(Context context, String shopId);

    Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId);

    Intent getKolCommentActivity(Context context, int postId, int rowNumber);
}
