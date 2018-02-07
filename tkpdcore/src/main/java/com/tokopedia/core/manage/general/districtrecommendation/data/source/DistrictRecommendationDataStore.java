package com.tokopedia.core.manage.general.districtrecommendation.data.source;

import com.google.gson.Gson;
import com.tokopedia.core.manage.general.districtrecommendation.data.entity.AddressResponseEntity;
import com.tokopedia.core.network.apiservices.kero.KeroAuthService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Irfan Khoirul on 17/11/17.
 */

public class DistrictRecommendationDataStore {
    private final KeroAuthService service;

    @Inject
    public DistrictRecommendationDataStore(KeroAuthService service) {
        this.service = service;
    }

    public Observable<AddressResponseEntity> getAddresses(TKPDMapParam<String, String> params) {
        return service.getApi().getDistrictRecommendation(params)
                .map(new Func1<Response<String>, AddressResponseEntity>() {
                    @Override
                    public AddressResponseEntity call(Response<String> stringResponse) {
                        return new Gson().fromJson(stringResponse.body(), AddressResponseEntity.class);
                    }
                });
    }
}
