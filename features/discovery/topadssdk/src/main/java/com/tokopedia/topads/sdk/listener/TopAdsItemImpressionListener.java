package com.tokopedia.topads.sdk.listener;

import com.tokopedia.topads.sdk.domain.model.Product;

/**
 * Created by errysuprayogi on 7/30/18.
 */

public interface TopAdsItemImpressionListener {

    void onImpressionProductAdsItem(int position, Product product);

}
