package com.tokopedia.common.travel.presentation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.di.CommonTravelComponent;
import com.tokopedia.common.travel.presentation.adapter.PhoneCodePickerAdapterTypeFactory;
import com.tokopedia.common.travel.presentation.model.CountryPhoneCode;
import com.tokopedia.common.travel.presentation.presenter.PhoneCodePickerPresenterImpl;
import com.tokopedia.common.travel.presentation.presenter.PhoneCodePickerView;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class PhoneCodePickerFragment extends BaseSearchListFragment<CountryPhoneCode, PhoneCodePickerAdapterTypeFactory> implements
        PhoneCodePickerView {

    public static final String EXTRA_SELECTED_PHONE_CODE = "EXTRA_SELECTED_PHONE_CODE";

    @Inject
    PhoneCodePickerPresenterImpl flightBookingPhoneCodePresenter;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(CommonTravelComponent.class)
                .inject(this);
    }

    @Override
    public void onItemClicked(CountryPhoneCode countryPhoneCode) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_PHONE_CODE, countryPhoneCode);
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
        View view = inflater.inflate(R.layout.fragment_phone_code_picker, container, false);
        view.requestFocus();
        return view;
    }

    @Override
    protected PhoneCodePickerAdapterTypeFactory getAdapterTypeFactory() {
        return new PhoneCodePickerAdapterTypeFactory();
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
}
