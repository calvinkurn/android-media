package com.tokopedia.digital_deals.view.presenter;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;


import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.GetSearchDealsListRequestUseCase;
import com.tokopedia.digital_deals.domain.GetSearchNextUseCase;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.ValuesItemDomain;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.activity.DealsLocationActivity;
import com.tokopedia.digital_deals.view.adapter.FiltersAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsSearchContract;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryViewModel;
import com.tokopedia.digital_deals.view.viewmodel.LocationViewModel;
import com.tokopedia.digital_deals.view.viewmodel.SearchViewModel;
import com.tokopedia.usecase.RequestParams;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;


public class DealsSearchPresenter
        extends BaseDaggerPresenter<DealsSearchContract.View>
        implements DealsSearchContract.Presenter {

    private GetSearchDealsListRequestUseCase getSearchDealsListRequestUseCase;
    private GetSearchNextUseCase getSearchNextUseCase;
    private String FRAGMENT_TAG = "FILTERFRAGMENT";
    private List<CategoryItemsViewModel> mTopDeals;
    private SearchDomainModel mSearchData;
    private ValuesItemDomain selectedTime;
    private String catgoryFilters;
    private String timeFilter;
    private String highlight;
    private boolean isLoading;
    private boolean isLastPage;
    private final int PAGE_SIZE = 20;
    private boolean SEARCH_SUBMITTED = false;
    RequestParams searchNextParams = RequestParams.create();

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

        getSearchDealsListRequestUseCase.execute(requestParams, new Subscriber<SearchDomainModel>() {
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
            public void onNext(SearchDomainModel searchDomainModel) {
                Log.d("MySearchDataaa", " " + searchDomainModel.toString() + " " + SEARCH_SUBMITTED);
                if (SEARCH_SUBMITTED)
                    getView().renderFromSearchResults(processSearchResponse(searchDomainModel), searchText);
                else
                    getView().setSuggestions(processSearchResponse(searchDomainModel), highlight);
                checkIfToLoad(getView().getLayoutManager());
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    @Override
    public void initialize() {
        mTopDeals = getView().getActivity().getIntent().getParcelableArrayListExtra("TOPDEALS");
        LocationViewModel location=Utils.getSingletonInstance().getLocation(getView().getActivity());
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
        Log.d("InsideSearchSubmitted", " " + SEARCH_SUBMITTED);
        getDealsListBySearch(searchText);

    }

    @Override
    public boolean onItemClick(int id) {
        if (id == R.id.tv_change_city) {
            getView().navigateToActivityRequest(new Intent(getView().getActivity(), DealsLocationActivity.class), DealsHomeActivity.REQUEST_CODE_DEALSLOCATIONACTIVITY);

        }else if (id == R.id.imageViewBack) {
            getView().goBack();
        }
        return true;
    }

    @Override
    public void onClickFilterItem(ValuesItemDomain filterItem, FiltersAdapter.FilterViewHolder viewHolder) {
        if (!filterItem.isMulti()) {
            if (!filterItem.getIsSelected()) {
                if (selectedTime != null)
                    selectedTime.setIsSelected(false);
                filterItem.setIsSelected(true);
                selectedTime = filterItem;
                timeFilter = selectedTime.getName();
            } else {
                filterItem.setIsSelected(false);
                selectedTime = null;
                timeFilter = "";
            }

        } else {
            if (!filterItem.getIsSelected()) {
                filterItem.setIsSelected(true);
                if (catgoryFilters != null && catgoryFilters.length() == 0) {
                    catgoryFilters.concat(",").concat(filterItem.getName());
                } else {
                    catgoryFilters = filterItem.getName();
                }
            } else {
                filterItem.setIsSelected(false);
                catgoryFilters.replace("," + filterItem.getName(), "");
            }
        }
    }


    @Override
    public void onSearchResultClick(CategoryItemsViewModel searchViewModel) {
        Intent detailsIntent = new Intent(getView().getActivity(), DealDetailsActivity.class);
        detailsIntent.putExtra(DealDetailsPresenter.HOME_DATA, searchViewModel);
        getView().navigateToActivity(detailsIntent);
    }

    @Override
    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
        checkIfToLoad(layoutManager);
    }

    private void loadMoreItems() {
        isLoading = true;

        getSearchNextUseCase.execute(searchNextParams, new Subscriber<SearchDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                isLoading = false;
            }

            @Override
            public void onNext(SearchDomainModel searchDomainModel) {
                isLoading = false;
                getView().removeFooter(SEARCH_SUBMITTED);
                if (SEARCH_SUBMITTED)
                    getView().addDealsToCards(processSearchResponse(searchDomainModel));
                else
                    getView().addDeals(processSearchResponse(searchDomainModel));
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
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                loadMoreItems();
            } else {
                getView().addFooter(SEARCH_SUBMITTED);
            }
        }
    }

    List<CategoryItemsViewModel> processSearchResponse(SearchDomainModel searchDomainModel) {
        mSearchData = searchDomainModel;
        String nexturl = mSearchData.getPage().getUriNext();
        if (nexturl != null && !nexturl.isEmpty() && nexturl.length() > 0) {
            searchNextParams.putString("nexturl", nexturl);
            isLastPage = false;
        } else {
            isLastPage = true;
        }
        List<CategoryItemsViewModel> categoryItemsViewModels = Utils.getSingletonInstance()
                .convertIntoCategoryListItemsViewModel(searchDomainModel.getDeals());
        return categoryItemsViewModels;
    }

}
