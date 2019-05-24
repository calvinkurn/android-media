package com.tokopedia.flight.booking.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.common.travel.presentation.presenter.PhoneCodePickerPresenterImpl;
import com.tokopedia.common.travel.presentation.presenter.PhoneCodePickerView;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.adapter.FlightBookingNationalityAdapterTypeFactory;
import com.tokopedia.common.travel.presentation.model.CountryPhoneCode;
import com.tokopedia.flight.common.util.FlightErrorUtil;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingNationalityFragment extends BaseSearchListFragment<CountryPhoneCode, FlightBookingNationalityAdapterTypeFactory> implements PhoneCodePickerView {

    public static final String EXTRA_SELECTED_COUNTRY = "EXTRA_SELECTED_COUNTRY";
    public static final String EXTRA_SEARCH_HINT = "EXTRA_SEARCH_HINT";

    @Inject
    PhoneCodePickerPresenterImpl flightBookingPhoneCodePresenter;

    public static FlightBookingNationalityFragment createInstance(String searchHint) {
        FlightBookingNationalityFragment fragment = new FlightBookingNationalityFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_SEARCH_HINT, searchHint);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        flightBookingPhoneCodePresenter.attachView(this);
        super.onViewCreated(view, savedInstanceState);

        searchInputView.setSearchHint(getArguments().getString(EXTRA_SEARCH_HINT));
    }

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
    public void loadData(int page) {
        flightBookingPhoneCodePresenter.getPhoneCodeList();
    }

    @Override
    protected FlightBookingNationalityAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightBookingNationalityAdapterTypeFactory();
    }

    @Override
    public void onItemClicked(CountryPhoneCode phoneCode) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_COUNTRY, phoneCode);
        KeyboardHandler.hideSoftKeyboard(getActivity());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
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
    protected String getMessageFromThrowable(Context context, Throwable t) {
        return FlightErrorUtil.getMessageFromException(context, t);
    }
}
