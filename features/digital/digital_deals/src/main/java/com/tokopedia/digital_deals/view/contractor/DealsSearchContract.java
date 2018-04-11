package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.ValuesItemDomain;
import com.tokopedia.digital_deals.view.adapter.FiltersAdapter;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.SearchViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;



public class DealsSearchContract {
    public interface IDealsSearchView extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderFromSearchResults(List<CategoryItemsViewModel> categoryItemsViewModels);

        void showProgressBar();

        void hideProgressBar();

        RequestParams getParams();

        android.view.View getRootView();

        FragmentManager getFragmentManagerInstance();

        void setTopEvents(List<SearchViewModel> searchViewModels);

        void setSuggestions(List<SearchViewModel> suggestions, String highlight);

        void removeFooter();

        void addFooter();

        void addEvents(List<SearchViewModel> searchViewModels);

        LinearLayoutManager getLayoutManager();
    }

    public interface IDealsSearchPresenter extends CustomerPresenter<IDealsSearchView> {

        void getDealsListBySearch(String searchText);

        void initialize();

        void onDestroy();

        void searchTextChanged(String searchText);

        void searchSubmitted(String searchText);

        boolean onOptionMenuClick(int id);

        void onClickFilterItem(ValuesItemDomain filterItem, FiltersAdapter.FilterViewHolder viewHolder);

        void onSearchResultClick(SearchViewModel searchViewModel);

        void onRecyclerViewScrolled(LinearLayoutManager layoutManager);
    }
}
