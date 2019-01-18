package com.tokopedia.district_recommendation.data.repository;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.district_recommendation.data.source.ShopAddressDataSource;
import com.tokopedia.network.utils.TKPDMapParam;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Irfan Khoirul on 19/11/18.
 */

public class ShopAddressRepository {

    private final ShopAddressDataSource dataStore;

    @Inject
    public ShopAddressRepository(ShopAddressDataSource dataStore) {
        this.dataStore = dataStore;
    }

    public Observable<Response<TokopediaWsV4Response>> getLocation(TKPDMapParam<String, String> parameters) {
        return dataStore.getLocation(parameters);
    }

}
