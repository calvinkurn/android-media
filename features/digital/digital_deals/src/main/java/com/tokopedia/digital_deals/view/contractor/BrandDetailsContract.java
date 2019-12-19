package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class BrandDetailsContract {

    public interface View extends CustomerView {

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderBrandDetails(List<ProductItem> productItems, Brand brand, int count);

        void showProgressBar();

        void hideProgressBar();

        void hideCollapsingHeader();

        void showCollapsingHeader();

        RequestParams getParams();

        android.view.View getRootView();

        void removeFooter();

        LinearLayoutManager getLayoutManager();

        void addFooter();

        void addDealsToCards(List<ProductItem> categoryList);
    }

    public interface Presenter extends CustomerPresenter<BrandDetailsContract.View> {

        void initialize();

        void onDestroy();

    }
}
