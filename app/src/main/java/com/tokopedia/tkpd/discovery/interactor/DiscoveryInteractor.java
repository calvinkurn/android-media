package com.tokopedia.tkpd.discovery.interactor;

import android.content.Context;

import com.tokopedia.tkpd.discovery.interfaces.DiscoveryListener;

import java.util.HashMap;

/**
 * Created by noiz354 on 3/17/16.
 */
public interface DiscoveryInteractor {
    void getProducts(HashMap<String, String> data);
    void getCatalogs(HashMap<String, String> data);
    void getShops(HashMap<String, String> data);
    void getDynamicAttribute(Context context, String source, String depId);
    void setDiscoveryListener(DiscoveryListener discoveryListener);
    void getHotListBanner(HashMap<String, String> data);
    void getTopAds(HashMap<String, String> data);
    void loadSearchSuggestion(String querySearch, String unique_id, int count);
    void deleteSearchHistory(String unique_id, String keyword, boolean clear_all);
}
