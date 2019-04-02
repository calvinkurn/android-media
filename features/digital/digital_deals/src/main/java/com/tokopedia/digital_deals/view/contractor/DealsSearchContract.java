package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.usecase.RequestParams;

import java.util.List;



public class DealsSearchContract {
    public interface View extends CustomerView {
        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderFromSearchResults();

        RequestParams getParams();

        android.view.View getRootView();

        void setTrendingDealsOrSuggestions(List<ProductItem> searchViewModels, boolean isTrendingDeals, String highlight, int count);

        void removeFooter();

        void addFooter();

        void addDealsToCards(List<ProductItem> productItems);

        LinearLayoutManager getLayoutManager();

        void goBack();

        void navigateToActivity(Intent intent);
    }

    public interface Presenter extends CustomerPresenter<View> {

        void getDealsListBySearch(String searchText);

        void initialize();

        void onDestroy();

        void searchTextChanged(String searchText);

        void searchSubmitted();

        boolean onItemClick(int id);

        void onRecyclerViewScrolled(LinearLayoutManager layoutManager);
    }
}
