package com.tokopedia.core.manage.people.address.listener;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tokopedia.core.addtocart.model.responseatcform.Destination;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.District;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.manage.people.address.fragment.adapter.ProvinceAdapter;
import com.tokopedia.core.manage.people.address.fragment.adapter.RegencyAdapter;

import java.util.List;

/**
 * Created by nisie on 9/6/16.
 */
public interface AddAddressFragmentView {
    void removeError();

    EditText getReceiverName();

    EditText getReceiverPhone();

    EditText getPostCode();

    EditText getAddressType();

    EditText getAddress();

    Spinner getSpinnerProvince();

    Spinner getSpinnerRegency();

    Spinner getSpinnerSubDistrict();

    String getString(int stringId);

    void setError(TextInputLayout textInputLayout, String errorMessage);

    TextInputLayout getReceiverNameLayout();

    TextInputLayout getAddressLayout();

    TextInputLayout getAddressTypeLayout();

    TextInputLayout getPostCodeLayout();

    TextInputLayout getReceiverPhoneLayout();

    TextView getSpinnerProvinceError();

    TextView getSpinnerRegencyError();

    TextView getSpinnerSubDistrictError();

    boolean isEdit();

    void setProvince(List<Province> provinces);

    void setDistrict(List<District> districts);

    void setCity(List<City> cities);

    void resetRegency();

    ProvinceAdapter getProvinceAdapter();

    Activity getActivity();

    void showLoadingRegency();

    void showLoadingDistrict();

    void resetSubDistrict();

    RegencyAdapter getRegencyAdapter();

    void showLoading();

    void hideSubDistrict();

    void finishLoading();

    void finishActivity(Destination address);

    void showErrorSnackbar(String errorMessage);

    void showErrorSnackbar(String message, View.OnClickListener listener);

    void setActionsEnabled(boolean b);

    Bundle getArguments();

    EditText getPassword();

    TextInputLayout getPasswordLayout();

    void setResult(Destination address);
}
