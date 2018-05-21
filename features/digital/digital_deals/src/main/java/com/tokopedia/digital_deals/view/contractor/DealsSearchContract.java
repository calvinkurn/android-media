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
import com.tokopedia.digital_deals.view.viewmodel.LocationViewModel;
import com.tokopedia.digital_deals.view.viewmodel.SearchViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;



public class DealsSearchContract {
    public interface View extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderFromSearchResults(List<CategoryItemsViewModel> categoryItemsViewModels, String searchText);

        void showProgressBar();

        void hideProgressBar();

        RequestParams getParams();

        android.view.View getRootView();

        FragmentManager getFragmentManagerInstance();

        void setTrendingDeals(List<CategoryItemsViewModel> searchViewModels, LocationViewModel location);

        void setSuggestions(List<CategoryItemsViewModel> suggestions, String highlight);

        void removeFooter(boolean searchSubmitted);

        void addFooter(boolean searchSubmitted);

        void addDealsToCards(List<CategoryItemsViewModel> categoryItemsViewModels);

        void addDeals(List<CategoryItemsViewModel> searchViewModels);

        LinearLayoutManager getLayoutManager();

        void goBack();

        void navigateToActivity(Intent intent);
    }

    public interface Presenter extends CustomerPresenter<View> {

        void getDealsListBySearch(String searchText);

        void initialize();

        void onDestroy();

        void searchTextChanged(String searchText);

        void searchSubmitted(String searchText);

        boolean onItemClick(int id);

        void onClickFilterItem(ValuesItemDomain filterItem, FiltersAdapter.FilterViewHolder viewHolder);

        void onSearchResultClick(CategoryItemsViewModel searchViewModel);

        void onRecyclerViewScrolled(LinearLayoutManager layoutManager);
    }
}
