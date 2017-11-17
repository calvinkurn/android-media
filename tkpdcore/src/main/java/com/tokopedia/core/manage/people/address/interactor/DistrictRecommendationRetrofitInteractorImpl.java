package com.tokopedia.core.manage.people.address.interactor;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.R;
import com.tokopedia.core.base.domain.DefaultSubscriber;
import com.tokopedia.core.manage.people.address.model.districtrecomendation.AddressResponse;
import com.tokopedia.core.manage.people.address.model.districtrecomendation.Token;
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

    private static final int NUMBER_OF_RETRY = 3;
    private KeroAuthService keroAuthService;
    private Context context;

    public DistrictRecommendationRetrofitInteractorImpl(Context context) {
        this.context = context;
        keroAuthService = new KeroAuthService(NUMBER_OF_RETRY);
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
                                    listener.onTimeout(context.getString(
                                            R.string.default_request_error_timeout));
                                }

                                @Override
                                public void onServerError() {
                                    listener.onTimeout(context.getString(
                                            R.string.default_request_error_internal_server));
                                }

                                @Override
                                public void onBadRequest() {
                                    listener.onTimeout(context.getString(
                                            R.string.default_request_error_bad_request));
                                }

                                @Override
                                public void onForbidden() {
                                    listener.onTimeout(context.getString(
                                            R.string.default_request_error_forbidden_auth));
                                }
                            }, stringResponse.code());
                        }
                    }
                });
    }

    public static TKPDMapParam<String, String> getParams(String query, Token token, int page) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(DistrictRecommendationRetrofitInteractor.Params.PAGE, String.valueOf(page));
        params.put(DistrictRecommendationRetrofitInteractor.Params.TOKEN,
                token.getDistrictRecommendation());
        params.put(DistrictRecommendationRetrofitInteractor.Params.UT,
                String.valueOf(token.getUnixTime()));
        params.put(DistrictRecommendationRetrofitInteractor.Params.QUERY, query);
        return params;
    }

}
