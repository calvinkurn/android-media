package com.tokopedia.district_recommendation.data.source;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.district_recommendation.data.service.MyShopAddressApi;
import com.tokopedia.network.utils.TKPDMapParam;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Irfan Khoirul on 19/11/18.
 */

public class ShopAddressDataSource {
    private final MyShopAddressApi myShopAddressApi;

    @Inject
    public ShopAddressDataSource(MyShopAddressApi myShopAddressApi) {
        this.myShopAddressApi = myShopAddressApi;
    }

    public Observable<Response<TokopediaWsV4Response>> getLocation(TKPDMapParam<String, String> params) {
        return myShopAddressApi.getLocation(params);
    }

}
