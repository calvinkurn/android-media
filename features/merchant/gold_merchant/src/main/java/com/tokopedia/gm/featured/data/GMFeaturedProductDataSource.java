package com.tokopedia.gm.featured.data;

import com.tokopedia.gm.featured.data.cloud.api.GMFeaturedProductApi;
import com.tokopedia.gm.featured.data.model.GMFeaturedProductDataModel;
import com.tokopedia.gm.featured.data.model.GMFeaturedProductModel;
import com.tokopedia.gm.featured.data.model.GMFeaturedProductSubmitModel;
import com.tokopedia.seller.shopscore.data.common.GetData;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 9/7/17.
 */

public class GMFeaturedProductDataSource {
    private GMFeaturedProductApi gmFeaturedProductApi;

    @Inject
    public GMFeaturedProductDataSource(GMFeaturedProductApi gmFeaturedProductApi) {
        this.gmFeaturedProductApi = gmFeaturedProductApi;
    }

    public Observable<GMFeaturedProductDataModel> productGETModelObservable(String shopId) {
        return gmFeaturedProductApi.getFeaturedProduct(shopId).map(new GetData<GMFeaturedProductDataModel>());
    }

    public Observable<GMFeaturedProductModel> productPOSTModelObservable(GMFeaturedProductSubmitModel GMFeaturedProductSubmitModel) {
        return gmFeaturedProductApi.postFeaturedProduct(GMFeaturedProductSubmitModel).map(new GetData<GMFeaturedProductModel>());
    }
}
