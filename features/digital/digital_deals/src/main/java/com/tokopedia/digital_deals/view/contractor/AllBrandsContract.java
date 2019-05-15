package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

public class AllBrandsContract {

    public interface View extends CustomerView {

        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void navigateToActivity(Intent intent);

        void renderBrandList(List<Brand> brandList, boolean isSearchSubmitted, Boolean... fromSearchResult);

        RequestParams getParams();

        android.view.View getRootView();

        void showProgressBar();

        void hideProgressBar();

        void showViews();

        void removeFooter();

        void addFooter();

        void addBrandsToCards(List<Brand> brandList);

        LinearLayoutManager getLayoutManager();

        void showEmptyView();

        void hideEmptyView();

        void startLocationFragment(List<Location> locations);
    }

    public interface Presenter extends CustomerPresenter<AllBrandsContract.View> {

        void initialize();

        void onDestroy();

        boolean onOptionMenuClick(int id);

        void onRecyclerViewScrolled(LinearLayoutManager layoutManager);

        void searchTextChanged(String searchText);

        void searchSubmitted(String searchText);

    }
}
