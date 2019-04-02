package com.tokopedia.gm.subscribe.data.source.product.cloud;

import com.tokopedia.gm.subscribe.data.source.product.cloud.api.GoldMerchantApi;
import com.tokopedia.gm.subscribe.data.source.product.cloud.model.GmServiceModel;
import com.tokopedia.gm.subscribe.data.tools.GetResponse;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 2/2/17.
 */
public class GmSubscribeProductCloud {

    private final GoldMerchantApi api;

    @Inject
    public GmSubscribeProductCloud(GoldMerchantApi api) {
        this.api = api;
    }

    public Observable<GmServiceModel> getProduct() {
        return api
                .getGoldMerchantProductList()
                .map(new GetResponse<GmServiceModel>());
    }

}
