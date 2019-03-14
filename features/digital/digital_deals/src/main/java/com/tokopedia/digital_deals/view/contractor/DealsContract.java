package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.customview.WrapContentHeightViewPager;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.CategoryItem;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

public class DealsContract {

    public interface View extends CustomerView {

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void navigateToActivity(Intent intent);

        void renderCategoryList(List<CategoryItem> categoryList, CategoryItem carousel, CategoryItem top);

        void renderBrandList(List<Brand> brandList);

        void renderCuratedDealsList(List<CategoryItem> categoryItems);

        void addDealsToCards(CategoryItem categoryItemsViewModels);

        RequestParams getParams();

        RequestParams getBrandParams();

        android.view.View getRootView();

        void showProgressBar();

        void hideProgressBar();

        void hideFavouriteButton();

        void showFavouriteButton();

        void startGeneralWebView(String url);

        LinearLayoutManager getLayoutManager();

        void removeFooter();

        void addFooter();

        void showViews();

        void startOrderListActivity();

        int getRequestCode();

        void startLocationFragment();

        void startDealsCategoryFragment(List<CategoryItem> categoryItems);
    }

    public interface Presenter extends CustomerPresenter<View> {

        void initialize();

        void onDestroy();

        void startBannerSlide(WrapContentHeightViewPager viewPager);

        void onBannerSlide(int page);

        boolean onOptionMenuClick(int id);

        void onRecyclerViewScrolled(LinearLayoutManager layoutManager);

    }
}
