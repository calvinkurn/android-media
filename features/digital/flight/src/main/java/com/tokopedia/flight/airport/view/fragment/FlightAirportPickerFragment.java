package com.tokopedia.flight.airport.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.di.DaggerFlightAirportComponent;
import com.tokopedia.flight.airport.di.FlightAirportModule;
import com.tokopedia.flight.airport.view.adapter.FlightAirportAdapterTypeFactory;
import com.tokopedia.flight.airport.view.adapter.FlightAirportClickListener;
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerContract;
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerPresenterImpl;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.common.di.component.FlightComponent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import static com.tokopedia.flight.airport.view.activity.FlightAirportPickerActivity.EXTRA_TOOLBAR_TITLE;

/**
 * Created by nathan on 10/19/17.
 */

public class FlightAirportPickerFragment extends BaseSearchListFragment<Visitable, FlightAirportAdapterTypeFactory>
        implements FlightAirportPickerContract.View, FlightAirportClickListener {

    public static final String EXTRA_SELECTED_AIRPORT = "extra_selected_aiport";

    private static final long DELAY_TEXT_CHANGED = TimeUnit.MILLISECONDS.toMillis(0);
    @Inject
    FlightAirportPickerPresenterImpl flightAirportPickerPresenter;
    private boolean isFirstTime = true;
    String searchHint;

    public static FlightAirportPickerFragment getInstance(String searchHint) {
        FlightAirportPickerFragment fragment = new FlightAirportPickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TOOLBAR_TITLE, searchHint);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_airport_picker, container, false);
        view.requestFocus();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchHint = getArguments().getString(EXTRA_TOOLBAR_TITLE);
        searchInputView.setSearchHint(String.format(
                getString(R.string.flight_label_search_hint_airport), searchHint));
    }

    @Override
    public void loadData(int page) {
        if (isFirstTime) searchInputView.setVisibility(View.GONE);
        flightAirportPickerPresenter.getPopularCityAirport();
    }

    @Override
    protected FlightAirportAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightAirportAdapterTypeFactory(this);
    }

    @Override
    protected void initInjector() {
        DaggerFlightAirportComponent.builder()
                .flightAirportModule(new FlightAirportModule())
                .flightComponent(getComponent(FlightComponent.class))
                .build()
                .inject(this);
        flightAirportPickerPresenter.attachView(this);
    }

    @Override
    public void onItemClicked(Visitable flightAirportDB) {
    }

    @Override
    public void onSearchSubmitted(String text) {
        flightAirportPickerPresenter.getSuggestionAirport(searchInputView.getSearchText());
    }

    @Override
    public void onSearchTextChanged(String text) {
        flightAirportPickerPresenter.getSuggestionAirport(text);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flightAirportPickerPresenter.detachView();
    }

    @Override
    protected long getDelayTextChanged() {
        return DELAY_TEXT_CHANGED;
    }

    @Override
    public void renderList(@NonNull List<Visitable> list) {
        if (isFirstTime) {
            searchInputView.setVisibility(View.VISIBLE);
            isFirstTime = false;
        }
        super.renderList(list);
    }

    @Override
    public void showGetAirportListLoading() {
        showLoading();
    }

    @Override
    public void hideGetAirportListLoading() {
        hideLoading();
    }

    @Override
    public void showLoading() {
        getAdapter().setElement(getLoadingModel());
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void airportClicked(FlightAirportViewModel airportViewModel) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_AIRPORT, airportViewModel);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public String getFilterText() {
        return searchInputView.getSearchText();
    }
}