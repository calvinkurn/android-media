package com.tokopedia.digital_deals.view.presenter;


import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;


import com.tokopedia.digital_deals.domain.GetSearchDealsListRequestUseCase;
import com.tokopedia.digital_deals.domain.GetSearchNextUseCase;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.ValuesItemDomain;
import com.tokopedia.digital_deals.view.adapter.FiltersAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsSearchContract;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.SearchViewModel;
import com.tokopedia.usecase.RequestParams;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;


public class DealsSearchPresenter
        extends BaseDaggerPresenter<DealsSearchContract.IDealsSearchView>
        implements DealsSearchContract.IDealsSearchPresenter {

    private GetSearchDealsListRequestUseCase getSearchDealsListRequestUseCase;
    private GetSearchNextUseCase getSearchNextUseCase;
    private String FRAGMENT_TAG = "FILTERFRAGMENT";
    private ArrayList<SearchViewModel> mTopDeals;
    private SearchDomainModel mSearchData;
    private ValuesItemDomain selectedTime;
    private String catgoryFilters;
    private String timeFilter;
    private String highlight;
    private boolean isLoading;
    private boolean isLastPage;
    private final int PAGE_SIZE = 20;
    RequestParams searchNextParams = RequestParams.create();

    @Inject
    public DealsSearchPresenter(GetSearchDealsListRequestUseCase getSearchEventsListRequestUseCase,
                                GetSearchNextUseCase searchNextUseCase) {
        this.getSearchDealsListRequestUseCase = getSearchEventsListRequestUseCase;
        this.getSearchNextUseCase = searchNextUseCase;
    }

    @Override
    public void getDealsListBySearch(String searchText) {
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
                Log.d("MySearchDataaa", " "+ searchDomainModel.toString());
                getView().setSuggestions(processSearchResponse(searchDomainModel), highlight);
                checkIfToLoad(getView().getLayoutManager());
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    @Override
    public void initialize() {
        mTopDeals = getView().getActivity().getIntent().getParcelableArrayListExtra("TOPDEALS");
        getView().setTopEvents(mTopDeals);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void searchTextChanged(String searchText) {
        if (searchText != null && !searchText.equals("")) {
            if (searchText.length() > 2) {
                getDealsListBySearch(searchText);
            }
            if (searchText.length() == 0) {
                getView().setTopEvents(mTopDeals);
            }
        } else {
            getView().setTopEvents(mTopDeals);
        }
    }

    @Override
    public void searchSubmitted(String searchText) {
        getDealsListBySearch(searchText);
    }

    @Override
    public boolean onOptionMenuClick(int id) {
//        if (id == R.id.action_filter) {
//            FragmentManager fragmentManager = getView().getFragmentManagerInstance();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            FilterFragment fragment = FilterFragment.newInstance(100);
//            fragment.setData(mSearchData.getFilters(), this);
//            transaction.add(R.id.main_content, fragment, FRAGMENT_TAG);
//            transaction.addToBackStack(FRAGMENT_TAG);
//            transaction.commit();
//        } else {
        getView().getActivity().onBackPressed();
        return true;
//        }
//        return true;
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
    public void onSearchResultClick(SearchViewModel searchViewModel) {
        CategoryItemsViewModel detailsViewModel = new CategoryItemsViewModel();
//        detailsViewModel.setTitle(searchViewModel.getTitle());
        detailsViewModel.setDisplayName(searchViewModel.getDisplayName());
        detailsViewModel.setUrl(searchViewModel.getUrl());
//        detailsViewModel.setImageApp(searchViewModel.getImageApp());
        detailsViewModel.setMinStartDate(searchViewModel.getMinStartDate());
        detailsViewModel.setMaxEndDate(searchViewModel.getMaxEndDate());
        detailsViewModel.setCityName(searchViewModel.getCityName());
        detailsViewModel.setSalesPrice(searchViewModel.getSalesPrice());
//        detailsViewModel.setIsTop(searchViewModel.getIsTop());
        detailsViewModel.setLongRichDesc("Fetching Description");
//        detailsViewModel.setTnc("Fetching TnC");
//        Intent detailsIntent = new Intent(getView().getActivity(), EventDetailsActivity.class);
//        detailsIntent.putExtra(EventDetailsActivity.FROM, EventDetailsActivity.FROM_HOME_OR_SEARCH);
//        detailsIntent.putExtra("homedata", detailsViewModel);
//        getView().getActivity().startActivity(detailsIntent);
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
                getView().removeFooter();
                getView().addEvents(processSearchResponse(searchDomainModel));
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
                getView().addFooter();
            }
        }
    }

    List<SearchViewModel> processSearchResponse(SearchDomainModel searchDomainModel) {
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
        return Utils.getSingletonInstance()
                .convertSearchResultsToModel(categoryItemsViewModels);
    }

}
