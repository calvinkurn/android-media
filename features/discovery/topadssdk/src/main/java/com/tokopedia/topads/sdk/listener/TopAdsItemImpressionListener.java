package com.tokopedia.topads.sdk.listener;

import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.Product;

/**
 * Created by errysuprayogi on 7/30/18.
 */

public abstract class TopAdsItemImpressionListener {

    public void onImpressionProductAdsItem(int position, Product product){
    }

    public void onImpressionHeadlineAdsItem(int position, CpmData data){
    }

}
