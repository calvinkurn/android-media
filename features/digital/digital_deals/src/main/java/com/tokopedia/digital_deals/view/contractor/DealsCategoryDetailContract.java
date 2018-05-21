package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryViewModel;
import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.PageViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

public class DealsCategoryDetailContract {

    public interface View extends CustomerView {

        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void navigateToActivity(Intent intent);

        void renderCategoryList(List<CategoryItemsViewModel> deals);

        void renderBrandList(List<BrandViewModel> brandList);

        RequestParams getParams();

        android.view.View getRootView();

        void showProgressBar();

        void hideProgressBar();

        void hideSearchButton();

        void showSearchButton();

        void startGeneralWebView(String url);

        void removeFooter();

        void addFooter();

        void addDealsToCards(List<CategoryItemsViewModel> categoryItemsViewModels);

        LinearLayoutManager getLayoutManager();

        void showViews();


    }

    public interface Presenter extends CustomerPresenter<DealsCategoryDetailContract.View> {

        void initialize();

        void onDestroy();

        boolean onOptionMenuClick(int id);

        void onRecyclerViewScrolled(LinearLayoutManager layoutManager);



    }
}
