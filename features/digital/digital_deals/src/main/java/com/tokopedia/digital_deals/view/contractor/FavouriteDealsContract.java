package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.model.ProductItem;

import java.util.List;

public class FavouriteDealsContract {

    public interface View extends CustomerView {

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void navigateToActivity(Intent intent);

        void renderDealsList(List<ProductItem> deals);

        android.view.View getRootView();

        void showProgressBar();

        void hideProgressBar();

        void removeFooter();

        void addFooter();

        void addDealsToCards(List<ProductItem> productItems);

        LinearLayoutManager getLayoutManager();

    }

    public interface Presenter extends CustomerPresenter<FavouriteDealsContract.View> {

        void initialize();

        void onDestroy();

        void onRecyclerViewScrolled(LinearLayoutManager layoutManager);

    }
}
