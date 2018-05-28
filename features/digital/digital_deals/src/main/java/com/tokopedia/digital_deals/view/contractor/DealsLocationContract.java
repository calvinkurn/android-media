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
import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.LocationViewModel;
import com.tokopedia.digital_deals.view.viewmodel.SearchViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

public class DealsLocationContract {

    public interface View extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderFromSearchResults(List<LocationViewModel> locationViewModelList, boolean isTopLocations);

        void showProgressBar();

        void hideProgressBar();

        void showViews();

        RequestParams getParams();

        android.view.View getRootView();

    }

    public interface Presenter extends CustomerPresenter<DealsLocationContract.View> {

        void getLocationListBySearch(String searchText);

        void onDestroy();

        void searchTextChanged(String searchText);

        void searchSubmitted(String searchText);

        void onSearchResultClick(LocationViewModel searchViewModel);
    }
}
