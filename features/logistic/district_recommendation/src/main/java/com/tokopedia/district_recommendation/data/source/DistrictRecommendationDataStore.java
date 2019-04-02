package com.tokopedia.district_recommendation.data.source;

import com.google.gson.Gson;
import com.tokopedia.district_recommendation.data.entity.AddressResponseEntity;
import com.tokopedia.district_recommendation.data.service.KeroApi;
import com.tokopedia.network.utils.TKPDMapParam;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Irfan Khoirul on 17/11/17.
 */

public class DistrictRecommendationDataStore {
    private final KeroApi keroApi;

    @Inject
    public DistrictRecommendationDataStore(KeroApi keroApi) {
        this.keroApi = keroApi;
    }

    public Observable<AddressResponseEntity> getAddresses(TKPDMapParam<String, String> params) {
        return keroApi.getDistrictRecommendation(params)
                .map(new Func1<Response<String>, AddressResponseEntity>() {
                    @Override
                    public AddressResponseEntity call(Response<String> stringResponse) {
                        return new Gson().fromJson(stringResponse.body(), AddressResponseEntity.class);
                    }
                });
    }
}
