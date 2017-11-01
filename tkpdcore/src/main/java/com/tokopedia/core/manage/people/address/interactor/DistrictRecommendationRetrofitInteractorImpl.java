package com.tokopedia.core.manage.people.address.interactor;

import com.google.gson.Gson;
import com.tokopedia.core.manage.people.address.model.districtrecomendation.Address;
import com.tokopedia.core.manage.people.address.model.districtrecomendation.AddressResponse;
import com.tokopedia.core.network.apiservices.kero.KeroAuthService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Irfan Khoirul on 01/11/17.
 */

public class DistrictRecommendationRetrofitInteractorImpl implements
        DistrictRecommendationRetrofitInteractor {

    private KeroAuthService keroAuthService;
    private ArrayList<Address> addresses;
    private AddressResponse addressResponse;
    private Subscription subscription;

    public DistrictRecommendationRetrofitInteractorImpl() {
        addresses = new ArrayList<>();
        keroAuthService = new KeroAuthService(3);
    }

    @Override
    public void getDistrictRecommendation(TKPDMapParam<String, String> params) {
        subscription = keroAuthService.getApi().getDistrictRecommendation(params)
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
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(700, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<AddressResponse>() {
                    @Override
                    public void call(AddressResponse addressResponse) {
                        addresses.addAll(addressResponse.getAddresses());
                    }
                });
    }

    @Override
    public void unSubscribe() {
        subscription.unsubscribe();
    }

}
