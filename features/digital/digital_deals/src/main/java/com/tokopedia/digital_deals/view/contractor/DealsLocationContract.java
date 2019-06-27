package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

public class DealsLocationContract {

    public interface View extends CustomerView {
        Activity getActivity();

        void renderFromSearchResults(List<Location> locationList, boolean isTopLocations, String... searchText);

        RequestParams getParams();

        android.view.View getRootView();

    }

    public interface Presenter extends CustomerPresenter<DealsLocationContract.View> {

        void getLocationListBySearch(String searchText);

        void onDestroy();

        void searchTextChanged(String searchText);

        void searchSubmitted(String searchText);
    }
}
