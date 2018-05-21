package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

public class DealAllBrandsContract {

    public interface View extends CustomerView {

        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void navigateToActivity(Intent intent);

        void renderBrandList(List<BrandViewModel> brandList, boolean isSearchSubmitted);

        RequestParams getParams();

        android.view.View getRootView();

        void showProgressBar();

        void hideProgressBar();

        void showViews();

        void startGeneralWebView(String url);

        void removeFooter();

        void addFooter();

        void addBrandsToCards(List<BrandViewModel> brandList);

        LinearLayoutManager getLayoutManager();
    }

    public interface Presenter extends CustomerPresenter<DealAllBrandsContract.View> {

        void initialize();

        void onDestroy();

        boolean onOptionMenuClick(int id);

        void onRecyclerViewScrolled(LinearLayoutManager layoutManager);

        void searchTextChanged(String searchText);

        void searchSubmitted(String searchText);

    }
}
