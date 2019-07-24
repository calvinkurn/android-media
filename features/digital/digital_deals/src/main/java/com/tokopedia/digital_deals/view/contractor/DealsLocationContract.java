package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

public class DealsLocationContract {

    public interface View extends CustomerView {
        Context getActivity();

        void renderPopularCities(List<Location> locationList, String... searchText);

        void renderPopularLocations(List<Location> locationList, String... searchText);

        LinearLayoutManager getLayoutManager();

        RequestParams getParams();

        void addFooter();

        void removeFooter();

        android.view.View getRootView();

    }

    public interface Presenter extends CustomerPresenter<DealsLocationContract.View> {

        void getLocationListBySearch(String searchText);

        void onDestroy();

        void searchTextChanged(String searchText);

        void searchSubmitted(String searchText);
    }
}
