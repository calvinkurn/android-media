package com.tokopedia.core.manage.people.address.interactor;

import com.google.gson.Gson;
import com.tokopedia.core.manage.people.address.model.districtrecomendation.AddressResponse;
import com.tokopedia.core.network.apiservices.kero.KeroAuthService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Irfan Khoirul on 01/11/17.
 */

public class DistrictRecommendationRetrofitInteractorImpl implements
        DistrictRecommendationRetrofitInteractor {

    private KeroAuthService keroAuthService;

    public DistrictRecommendationRetrofitInteractorImpl() {
        keroAuthService = new KeroAuthService(3);
    }

    @Override
    public Observable<AddressResponse> getDistrictRecommendation(TKPDMapParam<String, String> params) {
        return keroAuthService.getApi().getDistrictRecommendation(params)
                .flatMap(new Func1<Response<String>, Observable<AddressResponse>>() {
                    @Override
                    public Observable<AddressResponse> call(Response<String> stringResponse) {
                        AddressResponse addressResponse = new Gson().fromJson(stringResponse.body(),
                                AddressResponse.class);
                        return Observable.just(addressResponse);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
