package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

public class DealsCategoryDetailContract {

    public interface View extends CustomerView {

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void navigateToActivity(Intent intent);

        void renderCategoryList(List<ProductItem> deals, int count);

        void renderBrandList(List<Brand> brandList);

        String getCategoryParams();

        RequestParams getBrandParams();

        android.view.View getRootView();

        void showProgressBar();

        void hideProgressBar();

        void removeFooter();

        void addFooter();

        void addDealsToCards(List<ProductItem> productItems);

        LinearLayoutManager getLayoutManager();

        void showViews();

        void startLocationFragment(List<Location> locations);
    }

    public interface Presenter extends CustomerPresenter<DealsCategoryDetailContract.View> {

        void initialize();

        void onDestroy();

        boolean onOptionMenuClick(int id);

        void searchSubmitted(String searchText);

        void onRecyclerViewScrolled(LinearLayoutManager layoutManager);

    }
}
