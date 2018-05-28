package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

public class DealsBrandDetailsContract {

    public interface View extends CustomerView {

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderBrandDetails(List<CategoryItemsViewModel> categoryItemsViewModels, BrandViewModel brandViewModel);

        void showProgressBar();

        void hideProgressBar();

        void hideCollapsingHeader();

        void showCollapsingHeader();

        RequestParams getParams();

        android.view.View getRootView();

    }

    public interface Presenter extends CustomerPresenter<DealsBrandDetailsContract.View> {

        void initialize();

        void onDestroy();

    }
}
