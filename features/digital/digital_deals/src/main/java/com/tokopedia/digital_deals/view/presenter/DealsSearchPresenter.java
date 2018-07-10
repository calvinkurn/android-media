package com.tokopedia.digital_deals.view.presenter;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.getusecase.GetSearchDealsListRequestUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetSearchNextUseCase;
import com.tokopedia.digital_deals.view.model.response.SearchResponse;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.activity.DealsLocationActivity;
import com.tokopedia.digital_deals.view.contractor.DealsSearchContract;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;


public class DealsSearchPresenter
        extends BaseDaggerPresenter<DealsSearchContract.View>
        implements DealsSearchContract.Presenter {

    private GetSearchDealsListRequestUseCase getSearchDealsListRequestUseCase;
    private GetSearchNextUseCase getSearchNextUseCase;
    private List<ProductItem> mTopDeals;
    private SearchResponse mSearchData;
    private String highlight;
    private boolean isLoading;
    private boolean isLastPage;
    private boolean SEARCH_SUBMITTED = false;
    private RequestParams searchNextParams = RequestParams.create();

    @Inject
    public DealsSearchPresenter(GetSearchDealsListRequestUseCase getSearchDealsListRequestUseCase,
                                GetSearchNextUseCase searchNextUseCase) {
        this.getSearchDealsListRequestUseCase = getSearchDealsListRequestUseCase;
        this.getSearchNextUseCase = searchNextUseCase;
    }

    @Override
    public void getDealsListBySearch(final String searchText) {
        highlight = searchText;
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(getSearchDealsListRequestUseCase.TAG, searchText);
        getSearchDealsListRequestUseCase.setRequestParams(requestParams);
        getSearchDealsListRequestUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                e.printStackTrace();
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<SearchResponse>>(){
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                SearchResponse searchResponse = (SearchResponse) dataResponse.getData();
                if (SEARCH_SUBMITTED)
                    getView().renderFromSearchResults(processSearchResponse(searchResponse), searchText, searchResponse.getCount());
                else
                    getView().setSuggestions(processSearchResponse(searchResponse), highlight);
                checkIfToLoad(getView().getLayoutManager());
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    @Override
    public void initialize() {
        mTopDeals = getView().getActivity().getIntent().getParcelableArrayListExtra("TOPDEALS");
        Location location=Utils.getSingletonInstance().getLocation(getView().getActivity());
        getView().setTrendingDeals(mTopDeals, location);
    }

    @Override
    public void onDestroy() {
        getSearchDealsListRequestUseCase.unsubscribe();
        getSearchNextUseCase.unsubscribe();
    }

    @Override
    public void searchTextChanged(String searchText) {
        SEARCH_SUBMITTED = false;
        if (searchText != null && !searchText.equals("")) {
            if (searchText.length() > 2) {
                getDealsListBySearch(searchText);
            }
            if (searchText.length() == 0) {
                getView().setTrendingDeals(mTopDeals, Utils.getSingletonInstance().getLocation(getView().getActivity()));
            }
        } else {
            getView().setTrendingDeals(mTopDeals, Utils.getSingletonInstance().getLocation(getView().getActivity()));
        }
    }

    @Override
    public void searchSubmitted(String searchText) {
        SEARCH_SUBMITTED = true;
        getDealsListBySearch(searchText);

    }

    @Override
    public boolean onItemClick(int id) {
        if (id == R.id.tv_change_city) {
            getView().navigateToActivityRequest(new Intent(getView().getActivity(), DealsLocationActivity.class), DealsHomeActivity.REQUEST_CODE_DEALSLOCATIONACTIVITY);
            getView().getActivity().overridePendingTransition( R.anim.slide_in_up, R.anim.hold );
        }else if (id == R.id.imageViewBack) {
            getView().goBack();
        }
        return true;
    }


    @Override
    public void onSearchResultClick(ProductItem searchViewModel) {
        Intent detailsIntent = new Intent(getView().getActivity(), DealDetailsActivity.class);
        detailsIntent.putExtra(DealDetailsPresenter.HOME_DATA, searchViewModel.getSeoUrl());
        getView().navigateToActivity(detailsIntent);
    }

    @Override
    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
        checkIfToLoad(layoutManager);
    }

    private void loadMoreItems() {
        isLoading = true;

        getSearchNextUseCase.setRequestParams(searchNextParams);
        getSearchNextUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                isLoading = false;
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<SearchResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                SearchResponse searchResponse = (SearchResponse) dataResponse.getData();
                isLoading = false;
                getView().removeFooter(SEARCH_SUBMITTED);
                if (SEARCH_SUBMITTED)
                    getView().addDealsToCards(processSearchResponse(searchResponse));
                else
                    getView().addDeals(processSearchResponse(searchResponse));
                checkIfToLoad(getView().getLayoutManager());
            }
        });
    }

    private void checkIfToLoad(LinearLayoutManager layoutManager) {
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                loadMoreItems();
            } else {
                getView().addFooter(SEARCH_SUBMITTED);
            }
        }
    }

    private List<ProductItem> processSearchResponse(SearchResponse searchResponse) {
        mSearchData = searchResponse;
        String nexturl = mSearchData.getPage().getUriNext();
        if (nexturl != null && !nexturl.isEmpty() && nexturl.length() > 0) {
            searchNextParams.putString("nexturl", nexturl);
            isLastPage = false;
        } else {
            isLastPage = true;
        }
        List<ProductItem> productItems= searchResponse.getDeals();
        return productItems;
    }

}
