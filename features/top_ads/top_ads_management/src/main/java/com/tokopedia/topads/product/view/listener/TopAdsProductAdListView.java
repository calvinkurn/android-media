package com.tokopedia.topads.product.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;

import java.util.List;

/**
 * Created by hadi.putra on 04/05/18.
 */

public interface TopAdsProductAdListView extends CustomerView {
    void onSearchLoaded(List<ProductAd> productAds, boolean hasNextPage);

    void showListError(Throwable throwable);
}
