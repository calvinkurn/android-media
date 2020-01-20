package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.response.LocationResponse;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

public class DealsLocationContract {

    public interface View extends CustomerView {
        Context getActivity();

        void renderPopularCities(List<Location> locationList, String... searchText);

        LinearLayoutManager getLayoutManager();

        RequestParams getParams();

        android.view.View getRootView();

        void showProgressBar(boolean showProgressBar);

        void setCurrentLocation(LocationResponse locationResponse);

        void setDefaultLocation();
    }

    public interface Presenter extends CustomerPresenter<DealsLocationContract.View> {

        void onDestroy();
    }
}
