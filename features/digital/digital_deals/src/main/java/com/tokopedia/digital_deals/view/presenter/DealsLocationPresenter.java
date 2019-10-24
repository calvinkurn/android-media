package com.tokopedia.digital_deals.view.presenter;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.domain.getusecase.GetLocationCityUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetNearestLocationUseCase;
import com.tokopedia.digital_deals.view.contractor.DealsLocationContract;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.response.LocationResponse;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class DealsLocationPresenter extends BaseDaggerPresenter<DealsLocationContract.View> implements DealsLocationContract.Presenter {


    private GetLocationCityUseCase getLocationCityUseCase;
    private GetNearestLocationUseCase getNearestLocationUseCase;
    private List<Location> mTopLocations;

    @Inject
    public DealsLocationPresenter(GetLocationCityUseCase getLocationCityUseCase, GetNearestLocationUseCase getNearestLocationUseCase) {
        this.getLocationCityUseCase = getLocationCityUseCase;
        this.getNearestLocationUseCase = getNearestLocationUseCase;
    }

    @Override
    public void attachView(DealsLocationContract.View view) {
        super.attachView(view);
        getCities();
    }

    public void getCities() {
        if (getView() == null) {
            return;
        }
        getView().showProgressBar(true);
        getLocationCityUseCase.setRequestParams(getView().getParams());
        getLocationCityUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                e.printStackTrace();
                NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getCities();
                    }
                });
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<LocationResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                LocationResponse locationResponse = (LocationResponse) dataResponse.getData();
                mTopLocations = locationResponse.getLocations();
                getView().showProgressBar(false);
                getView().renderPopularCities(mTopLocations);
            }
        });
    }

    public void getNearestLocation(String coordinates) {
        if (getView() == null) {
            return;
        }
        RequestParams params = RequestParams.create();
        params.putString(Utils.LOCATION_COORDINATES, coordinates);
        getNearestLocationUseCase.setRequestParams(params);
        getNearestLocationUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<LocationResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                LocationResponse locationResponse = (LocationResponse) dataResponse.getData();
                if (locationResponse != null && locationResponse.getLocations() != null) {
                    getView().setCurrentLocation(locationResponse);
                } else {
                    getView().setDefaultLocation();
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        getLocationCityUseCase.unsubscribe();
    }
}
