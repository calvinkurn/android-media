package com.tokopedia.logisticaddaddress.data.source;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.logisticaddaddress.data.service.MyShopAddressApi;

import java.util.Map;

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

    public Observable<Response<TokopediaWsV4Response>> getLocation(Map<String, String> params) {
        return myShopAddressApi.getLocation(params);
    }

}
