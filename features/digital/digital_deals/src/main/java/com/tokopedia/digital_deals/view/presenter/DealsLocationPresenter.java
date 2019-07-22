package com.tokopedia.digital_deals.view.presenter;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.digital_deals.domain.getusecase.GetLocationCityUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetLocationListRequestUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetNextPopularLocationsUseCase;
import com.tokopedia.digital_deals.view.contractor.DealsLocationContract;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.Page;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.response.CategoryDetailsResponse;
import com.tokopedia.digital_deals.view.model.response.LocationResponse;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

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
    private GetNextPopularLocationsUseCase getNextPopularLocationsUseCase;
    private List<Location> mTopLocations;
    private List<Location> mAllLocations;
    private Page page;
    private boolean isLoading;
    private boolean isLastPage;
    private RequestParams nextUrlParams = RequestParams.create();

    @Inject
    public DealsLocationPresenter(GetLocationListRequestUseCase getLocationListRequestUseCase, GetLocationCityUseCase getLocationCityUseCase, GetNextPopularLocationsUseCase getNextPopularLocationsUseCase) {
        this.getSearchLocationListRequestUseCase = getLocationListRequestUseCase;
        this.getLocationCityUseCase = getLocationCityUseCase;
        this.getNextPopularLocationsUseCase = getNextPopularLocationsUseCase;
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
                page = locationResponse.getPage();
                getNextPageUrl();
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

    public void loadMore() {
        isLoading = true;
        getNextPopularLocationsUseCase.setRequestParams(nextUrlParams);
        getNextPopularLocationsUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                isLoading = false;
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<LocationResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                isLoading = false;
                LocationResponse locationResponse = (LocationResponse) dataResponse.getData();
                mTopLocations = locationResponse.getLocations();
                mAllLocations = locationResponse.getLocations();
                getView().removeFooter();
                getView().renderPopularLocations(mTopLocations);
                page = locationResponse.getPage();
            }
        });

    }

    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
        int visibleItemCount = layoutManager.findLastVisibleItemPosition();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition + 1) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                isLoading = true;
                loadMore();
            }
        } else {
            getView().addFooter();
        }
    }

    private void getNextPageUrl() {
        if (page != null) {
            if (!TextUtils.isEmpty(page.getUriNext())) {
                nextUrlParams.putString(Utils.NEXT_URL, page.getUriNext());
                isLastPage = false;
            } else {
                isLastPage = true;
            }
        }
    }
}
