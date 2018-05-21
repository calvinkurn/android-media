package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

public class DealsContract {

    public interface View extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void navigateToActivity(Intent intent);

        void renderCategoryList(List<CategoryViewModel> categoryList, CategoryViewModel carousel, CategoryViewModel top);

        void renderBrandList(List<BrandViewModel> brandList);

        void addDealsToCards(CategoryViewModel categoryItemsViewModels);

        RequestParams getParams();

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

    }

    public interface Presenter extends CustomerPresenter<View> {

        void initialize();

        void onDestroy();

        void startBannerSlide(TouchViewPager viewPager);

        void onBannerSlide(int page);

        boolean onOptionMenuClick(int id);

        void onRecyclerViewScrolled(LinearLayoutManager layoutManager);

    }
}
