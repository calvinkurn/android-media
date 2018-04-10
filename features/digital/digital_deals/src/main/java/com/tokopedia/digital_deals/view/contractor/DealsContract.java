package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.digital_deals.view.viewmodel.CategoryViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

public class DealsContract {

    public interface View extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderCategoryList(List<CategoryViewModel> categoryList);

        RequestParams getParams();

        android.view.View getRootView();

        void showProgressBar();

        void hideProgressBar();

        void hideSearchButton();

        void showSearchButton();

    }

    public interface Presenter extends CustomerPresenter<View> {

        void initialize();

        void onDestroy();

        void startBannerSlide(TouchViewPager viewPager);

        void onBannerSlide(int page);

        boolean onOptionMenuClick(int id);
    }
}
