package com.tokopedia.discovery.common.manager;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.track.TrackApp;

public class SimilarSearchManager {

    public static final String EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID";

    private Context context;

    private SimilarSearchManager(Context context) {
        this.context = context;
    }

    public static SimilarSearchManager getInstance(Context context) {
        return new SimilarSearchManager(context);
    }

    public void startSimilarSearchIfEnable(String queryKey, String productId) {
        trackEventProductLongPress(queryKey, productId);
        Intent intent = RouteManager.getIntent(context, ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        context.startActivity(intent);
    }

    private void trackEventProductLongPress(String keyword, String productId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                "clickSearchResult",
                "search result",
                "click - long press product",
                String.format("Keyword: %s - product id: %s", keyword, productId));
    }
}
