package com.tokopedia.digital_deals.view.presenter;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
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
    private List<Location> mTopLocations;
    private List<Location> mAllLocations;

    @Inject
    public DealsLocationPresenter(GetLocationListRequestUseCase getLocationListRequestUseCase) {
        this.getSearchLocationListRequestUseCase = getLocationListRequestUseCase;
    }

    @Override
    public void getLocationListBySearch(String searchText) {
        List<Location> locationList = new ArrayList<>();
        if (mAllLocations != null) {
            for (Location location : mAllLocations)
                if (location.getName().trim().toLowerCase().contains(searchText.trim().toLowerCase()))
                    locationList.add(location);
        }
        getView().renderFromSearchResults(locationList, !isTopLocations, searchText);
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
                getView().renderFromSearchResults(mTopLocations, isTopLocations);
            }
        });
    }


    @Override
    public void onDestroy() {
        getSearchLocationListRequestUseCase.unsubscribe();
    }

    @Override
    public void searchTextChanged(String searchText) {
        SEARCH_SUBMITTED = false;
        if (searchText != null && !searchText.equals("")) {
            if (searchText.length() > 0)
                getLocationListBySearch(searchText);
            if (searchText.length() == 0)
                getView().renderFromSearchResults(mTopLocations, isTopLocations);
        } else
            getView().renderFromSearchResults(mTopLocations, isTopLocations);

    }

    @Override
    public void searchSubmitted(String searchText) {
        SEARCH_SUBMITTED = true;
        getLocationListBySearch(searchText);
    }
}
