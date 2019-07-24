package com.tokopedia.search.similarsearch;

import android.content.Context;

import com.tokopedia.discovery.newdiscovery.constant.SearchEventTracking;
import com.tokopedia.discovery.similarsearch.view.SimilarSearchActivity;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.track.TrackApp;

public class SimilarSearchManager {

    private Context context;
    private static final String FIREBASE_SIMILAR_SEARCH_REMOTE_CONFIG_KEY = "app_enable_similar_search";
    private RemoteConfig remoteConfig;

    private SimilarSearchManager(Context context) {
        this.context = context;
        initRemoteConfig(context);
    }

    public static SimilarSearchManager getInstance(Context context) {
        return new SimilarSearchManager(context);
    }

    private void initRemoteConfig(Context context) {
        remoteConfig = new FirebaseRemoteConfigImpl(context);
    }

    public boolean isSimilarSearchEnable() {
        return remoteConfig.getBoolean(FIREBASE_SIMILAR_SEARCH_REMOTE_CONFIG_KEY,true);
    }

    public void startSimilarSearchIfEnable(String queryKey, ProductItemViewModel item) {
        if (isSimilarSearchEnable()) {
            trackEventProductLongPress(queryKey, item.getProductID());
            context.startActivity(SimilarSearchActivity.getIntent(context, item.getProductID()));
        }
    }

    private void trackEventProductLongPress(String keyword, String productId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.SEARCH_RESULT,
                SearchEventTracking.Category.EventSearchResult,
                SearchEventTracking.Action.EventLongPressProduct,
                String.format(SearchEventTracking.Label.LabelKeywordProduct, keyword, productId));
    }
}
