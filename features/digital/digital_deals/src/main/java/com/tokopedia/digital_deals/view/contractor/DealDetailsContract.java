package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.OutletViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

public class DealDetailsContract {

    public interface View extends CustomerView{

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderDealDetails(DealsDetailsViewModel detailsViewModel);

        void showProgressBar();

        void hideProgressBar();

        RequestParams getParams();

        android.view.View getRootView();

        void hideShareButton();

        void showShareButton();

        void hideCollapsingHeader();

        void showCollapsingHeader();

    }
    public interface Presenter extends CustomerPresenter<DealDetailsContract.View> {

        void initialize();

        void onDestroy();

        boolean onOptionMenuClick(int id);

        List<OutletViewModel> getAllOutlets();


    }
}
