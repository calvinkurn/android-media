package com.tokopedia.flight.airport.view.presenter;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by nabillasabbaha on 06/03/19.
 */
public interface FlightAirportPickerContract {

    interface View extends BaseListViewListener<Visitable> {

        void showGetAirportListLoading();

        void hideGetAirportListLoading();

        Activity getActivity();

        void showLoading();
    }

    interface Presenter extends CustomerPresenter<View> {

        void getPopularCityAirport();

        void getSuggestionAirport(String text);
    }
}
