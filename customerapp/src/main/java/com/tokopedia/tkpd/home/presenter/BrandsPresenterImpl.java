package com.tokopedia.tkpd.home.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.apiservices.etc.apis.home.CategoryApi;
import com.tokopedia.core.network.entity.home.Brands;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.tkpd.home.OnGetBrandsListener;
import com.tokopedia.tkpd.home.interactor.HomeMenuInteractor;
import com.tokopedia.tkpd.home.interactor.HomeMenuInteractorImpl;

import retrofit2.Response;
import rx.Subscriber;

/**
 * Created by Herdi_WORK on 03.02.17.
 */

public class BrandsPresenterImpl implements BrandsPresenter, ErrorListener {

    private HomeMenuInteractor homeMenuInteractor;
    private OnGetBrandsListener brandsListener;

    public BrandsPresenterImpl(OnGetBrandsListener onGetBrands) {
        homeMenuInteractor = new HomeMenuInteractorImpl();
        brandsListener = onGetBrands;
    }

    @Override
    public void fetchBrands() {
        Subscriber<Response<Brands>> subscriber = getSubscriber();
        homeMenuInteractor.fetchBrands(CategoryApi.FILTER_ANDROID_DEVICE, subscriber);
    }

    @Override
    public void onDestroy() {
        homeMenuInteractor.removeSubscription();
        brandsListener = null;
    }

    private Subscriber<Response<Brands>> getSubscriber() {
        Subscriber<Response<Brands>> subscriber = new Subscriber<Response<Brands>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response<Brands> response) {

                if (response != null && response.body() != null)
                    brandsListener.onSuccess(response.body());
                else
                    new ErrorHandler(BrandsPresenterImpl.this, response.code());

            }
        };


        return subscriber;
    }

    @Override
    public void onUnknown() {

    }

    @Override
    public void onTimeout() {

    }

    @Override
    public void onServerError() {

    }

    @Override
    public void onBadRequest() {

    }

    @Override
    public void onForbidden() {

    }
}
