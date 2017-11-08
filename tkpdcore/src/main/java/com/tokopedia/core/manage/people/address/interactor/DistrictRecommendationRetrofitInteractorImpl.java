package com.tokopedia.core.manage.people.address.interactor;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.DefaultSubscriber;
import com.tokopedia.core.manage.people.address.model.districtrecomendation.AddressResponse;
import com.tokopedia.core.network.apiservices.kero.KeroAuthService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
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
    public void getDistrictRecommendation(TKPDMapParam<String, String> params,
                                          final DistrictRecommendationListener listener) {
        keroAuthService.getApi().getDistrictRecommendation(params)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<Response<String>>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listener.onNoConnection();
                    }

                    @Override
                    public void onNext(Response<String> stringResponse) {
                        if (stringResponse.isSuccessful()) {
                            listener.onSuccess(new Gson().fromJson(stringResponse.body(),
                                    AddressResponse.class));
                        } else {
                            new ErrorHandler(new ErrorListener() {
                                @Override
                                public void onUnknown() {
                                    listener.onNoConnection();
                                }

                                @Override
                                public void onTimeout() {
                                    listener.onTimeout("Mohon ulangi beberapa saat lagi");
                                }

                                @Override
                                public void onServerError() {
                                    listener.onTimeout("Mohon ulangi beberapa saat lagi");
                                }

                                @Override
                                public void onBadRequest() {
                                    listener.onTimeout("Mohon ulangi beberapa saat lagi");
                                }

                                @Override
                                public void onForbidden() {
                                    listener.onTimeout("Mohon ulangi beberapa saat lagi");
                                }
                            }, stringResponse.code());
                        }
                    }
                });
    }

}
