package com.tokopedia.flight.booking.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.adapter.FlightBookingPhoneCodeAdapterTypeFactory;
import com.tokopedia.flight.booking.view.presenter.FlightBookingPhoneCodePresenterImpl;
import com.tokopedia.flight.booking.view.presenter.FlightBookingPhoneCodeView;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.common.util.FlightErrorUtil;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FLightBookingPhoneCodeFragment extends BaseSearchListFragment<FlightBookingPhoneCodeViewModel, FlightBookingPhoneCodeAdapterTypeFactory> implements
        FlightBookingPhoneCodeView {

    public static final String EXTRA_SELECTED_PHONE_CODE = "EXTRA_SELECTED_PHONE_CODE";

    @Inject
    FlightBookingPhoneCodePresenterImpl flightBookingPhoneCodePresenter;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightBookingComponent.class)
                .inject(this);
    }

    @Override
    public void onItemClicked(FlightBookingPhoneCodeViewModel flightBookingPhoneCodeViewModel) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_PHONE_CODE, flightBookingPhoneCodeViewModel);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void loadData(int page) {
        flightBookingPhoneCodePresenter.attachView(this);
        flightBookingPhoneCodePresenter.getPhoneCodeList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_airport_picker, container, false);
        view.requestFocus();
        return view;
    }

    @Override
    protected FlightBookingPhoneCodeAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightBookingPhoneCodeAdapterTypeFactory();
    }

    @Override
    public void onSearchSubmitted(String text) {
        flightBookingPhoneCodePresenter.getPhoneCodeList(text);
    }

    @Override
    public void onSearchTextChanged(String text) {
        flightBookingPhoneCodePresenter.getPhoneCodeList(text);
    }

    @Override
    public void onDestroyView() {
        flightBookingPhoneCodePresenter.onDestroyView();
        super.onDestroyView();
    }

    @Override
    protected String getMessageFromThrowable(Context context, Throwable t) {
        return FlightErrorUtil.getMessageFromException(context, t);
    }
}
