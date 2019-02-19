package com.tokopedia.digital_deals.view.presenter;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.getusecase.GetSearchDealsListRequestUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetSearchNextUseCase;
import com.tokopedia.digital_deals.view.TopDealsCacheHandler;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.activity.DealsLocationActivity;
import com.tokopedia.digital_deals.view.contractor.DealsSearchContract;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.response.SearchResponse;
import com.tokopedia.digital_deals.view.utils.Utils;
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
        Location location = Utils.getSingletonInstance().getLocation(getView().getActivity());
        requestParams.putInt(Utils.QUERY_PARAM_CITY_ID, location.getId());
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
                Type token = new TypeToken<DataResponse<SearchResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                SearchResponse searchResponse = (SearchResponse) dataResponse.getData();
                getView().setTrendingDealsOrSuggestions(processSearchResponse(searchResponse), false, highlight, searchResponse.getCount());
                checkIfToLoad(getView().getLayoutManager());
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    @Override
    public void initialize() {
//        mTopDeals = getView().getActivity().getIntent().getParcelableArrayListExtra("TOPDEALS");
        mTopDeals = TopDealsCacheHandler.init().getTopDeals();
        if (mTopDeals != null && mTopDeals.size() > 0) {
            getView().setTrendingDealsOrSuggestions(mTopDeals, true, null, mTopDeals.size());
        }
    }

    @Override
    public void onDestroy() {
        getSearchDealsListRequestUseCase.unsubscribe();
        getSearchNextUseCase.unsubscribe();
    }

    @Override
    public void searchTextChanged(String searchText) {
        if (!TextUtils.isEmpty(searchText)) {
            getDealsListBySearch(searchText);

        } else {
            getSearchDealsListRequestUseCase.unsubscribe();
            getSearchNextUseCase.unsubscribe();
            getView().setTrendingDealsOrSuggestions(mTopDeals, true, null, mTopDeals.size());
        }
    }

    @Override
    public void searchSubmitted(String searchText) {
        getView().renderFromSearchResults();
    }

    @Override
    public boolean onItemClick(int id) {
        if (id == R.id.tv_change_city) {
            getView().navigateToActivityRequest(new Intent(getView().getActivity(), DealsLocationActivity.class), DealsHomeActivity.REQUEST_CODE_DEALSLOCATIONACTIVITY);
            getView().getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.hold);
        } else if (id == R.id.imageViewBack) {
            getView().goBack();
        }
        return true;
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
                getView().removeFooter();
                getView().addDealsToCards(processSearchResponse(searchResponse));
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
                getView().addFooter();
            }
        }
    }

    private List<ProductItem> processSearchResponse(SearchResponse searchResponse) {
        mSearchData = searchResponse;
        String nexturl = mSearchData.getPage().getUriNext();
        if (!TextUtils.isEmpty(nexturl)) {
            searchNextParams.putString(Utils.NEXT_URL, nexturl);
            isLastPage = false;
        } else {
            isLastPage = true;
        }
        return searchResponse.getDeals();
    }

}
