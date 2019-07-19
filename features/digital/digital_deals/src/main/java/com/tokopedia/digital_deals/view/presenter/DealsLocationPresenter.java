package com.tokopedia.digital_deals.view.presenter;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.digital_deals.domain.getusecase.GetLocationCityUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetLocationListRequestUseCase;
import com.tokopedia.digital_deals.view.contractor.DealsLocationContract;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.response.LocationResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class DealsLocationPresenter extends BaseDaggerPresenter<DealsLocationContract.View> implements DealsLocationContract.Presenter {


    private boolean SEARCH_SUBMITTED = false;
    private boolean isTopLocations = true;

    private GetLocationListRequestUseCase getSearchLocationListRequestUseCase;
    private GetLocationCityUseCase getLocationCityUseCase;
    private List<Location> mTopLocations;
    private List<Location> mAllLocations;

    @Inject
    public DealsLocationPresenter(GetLocationListRequestUseCase getLocationListRequestUseCase, GetLocationCityUseCase getLocationCityUseCase) {
        this.getSearchLocationListRequestUseCase = getLocationListRequestUseCase;
        this.getLocationCityUseCase = getLocationCityUseCase;
    }

    @Override
    public void attachView(DealsLocationContract.View view) {
        super.attachView(view);
        getLocations();
        getCities();
    }

    @Override
    public void getLocationListBySearch(String searchText) {
        List<Location> locationList = new ArrayList<>();
        if (mAllLocations != null) {
            for (Location location : mAllLocations)
                if (location.getName().trim().toLowerCase().contains(searchText.trim().toLowerCase()))
                    locationList.add(location);
        }
        getView().renderPopularCities(locationList, searchText);
    }

    public void getLocations() {
        getSearchLocationListRequestUseCase.setRequestParams(getView().getParams());
        getSearchLocationListRequestUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
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
                        getLocations();
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
                mAllLocations = locationResponse.getLocations();
                getView().renderPopularLocations(mTopLocations);
            }
        });
    }

    public void getCities() {
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
                mAllLocations = locationResponse.getLocations();
                getView().renderPopularCities(mTopLocations);
            }
        });
    }


    @Override
    public void onDestroy() {
        getSearchLocationListRequestUseCase.unsubscribe();
        getLocationCityUseCase.unsubscribe();
    }

    @Override
    public void searchTextChanged(String searchText) {
        SEARCH_SUBMITTED = false;
        if (searchText != null && !searchText.equals("")) {
            if (searchText.length() > 0)
                getLocationListBySearch(searchText);
            if (searchText.length() == 0)
                getView().renderPopularCities(mTopLocations);
        } else
            getView().renderPopularCities(mTopLocations);

    }

    @Override
    public void searchSubmitted(String searchText) {
        SEARCH_SUBMITTED = true;
        getLocationListBySearch(searchText);
    }
}
