package com.tokopedia.core.manage.people.address.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.District;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.fragment.adapter.ProvinceAdapter;
import com.tokopedia.core.manage.people.address.fragment.adapter.RegencyAdapter;
import com.tokopedia.core.manage.people.address.fragment.adapter.SubDistrictAdapter;
import com.tokopedia.core.manage.people.address.listener.AddAddressFragmentView;
import com.tokopedia.core.manage.people.address.model.Destination;
import com.tokopedia.core.manage.people.address.presenter.AddAddressPresenter;
import com.tokopedia.core.manage.people.address.presenter.AddAddressPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by nisie on 9/6/16.
 */
public class AddAddressFragment extends BasePresenterFragment<AddAddressPresenter>
        implements AddAddressFragmentView, ManageAddressConstant {
    private final String ARG_STATE_PROVINCE = "provincesData";
    private final String ARG_STATE_CITY = "citiesData";
    private final String ARG_STATE_DISTRICT = "districtsData";

    @BindView(R2.id.receiver_name_layout)
    TextInputLayout receiverNameLayout;

    @BindView(R2.id.receiver_name)
    EditText receiverNameEditText;

    @BindView(R2.id.address_type_layout)
    TextInputLayout addressTypeLayout;

    @BindView(R2.id.address_type)
    EditText addressTypeEditText;

    @BindView(R2.id.address_layout)
    TextInputLayout addressLayout;

    @BindView(R2.id.address)
    EditText addressEditText;

    @BindView(R2.id.post_code_layout)
    TextInputLayout postCodeLayout;

    @BindView(R2.id.post_code)
    EditText postcodeEditText;

    @BindView(R2.id.receiver_phone_layout)
    TextInputLayout receiverPhoneLayout;

    @BindView(R2.id.receiver_phone)
    EditText receiverPhoneEditText;

    @BindView(R2.id.province_error)
    TextView provinceError;

    @BindView(R2.id.regency_error)
    TextView regencyError;

    @BindView(R2.id.sub_district_error)
    TextView subDistrictError;

    @BindView(R2.id.regency)
    Spinner spinnerRegency;

    @BindView(R2.id.sub_district)
    Spinner spinnerSubDistrict;

    @BindView(R2.id.provinsi)
    Spinner spinnerProvince;

    @BindView(R2.id.regency_progress)
    ProgressBar progressRegency;

    @BindView(R2.id.district_progress)
    ProgressBar progressDistrict;

    @BindView(R2.id.regency_title)
    TextView regencyTitle;

    @BindView(R2.id.district_title)
    TextView districtTitle;

    @BindView(R2.id.layout_value_location)
    View chooseLocation;

    @BindView(R2.id.value_location)
    EditText locationEditText;

    @BindView(R2.id.password_layout)
    TextInputLayout passwordLayout;

    @BindView(R2.id.password)
    EditText password;

    @BindView(R2.id.save_button)
    TextView saveButton;

    ProvinceAdapter provinceAdapter;
    RegencyAdapter regencyAdapter;
    SubDistrictAdapter subDistrictAdapter;

    TkpdProgressDialog mProgressDialog;
    Boolean isEdit = false;

    List<Province> mProvinces;
    List<City> mCities;
    List<District> mDistricts;

    public static AddAddressFragment createInstance(Bundle extras) {
        AddAddressFragment fragment = new AddAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putAll(extras);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        isEdit = getArguments().getBoolean(IS_EDIT, false);
        presenter.getListProvince();
    }

    private void setEditParam(Destination address) {
        addressTypeEditText.setText(MethodChecker.fromHtml(address.getAddressName()));
        addressEditText.setText(address.getAddressStreet());
        receiverNameEditText.setText(MethodChecker.fromHtml(address.getReceiverName()));
        receiverPhoneEditText.setText(address.getReceiverPhone());
        postcodeEditText.setText(address.getPostalCode());
        if (address.getLatitude() != null &&
                address.getLongitude() != null &&
                !address.getLatitude().equals("") &&
                !address.getLongitude().equals("")
                ) {
            locationEditText.setText(address.getGeoLocation(getActivity()));
            presenter.setLatLng(address.getLatitude(), address.getLongitude());
        }
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putParcelableArrayList(ARG_STATE_PROVINCE, (ArrayList<? extends Parcelable>) this.mProvinces);
        state.putParcelableArrayList(ARG_STATE_CITY, (ArrayList<? extends Parcelable>) this.mCities);
        state.putParcelableArrayList(ARG_STATE_DISTRICT, (ArrayList<? extends Parcelable>) this.mDistricts);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        if (savedState != null) {
            this.mProvinces = savedState.getParcelableArrayList(ARG_STATE_PROVINCE);
            this.mCities = savedState.getParcelableArrayList(ARG_STATE_CITY);
            this.mDistricts = savedState.getParcelableArrayList(ARG_STATE_DISTRICT);
            if (this.mProvinces != null && this.mProvinces.size() > 0) {
                setProvince(this.mProvinces);
            }
            if (this.mCities != null && this.mCities.size() > 0) {
                setCity(this.mCities);
            }
            if (this.mDistricts != null && this.mDistricts.size() > 0) {
                setDistrict(this.mDistricts);
            }
        }
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new AddAddressPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_add_address;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTextWatcher();
    }

    private void setTextWatcher() {
        receiverNameEditText.addTextChangedListener(watcher(receiverNameLayout));
        addressEditText.addTextChangedListener(watcher(addressLayout));
        addressTypeEditText.addTextChangedListener(watcher(addressTypeLayout));
        postcodeEditText.addTextChangedListener(watcher(postCodeLayout));
        receiverPhoneEditText.addTextChangedListener(watcher(receiverPhoneLayout));
        password.addTextChangedListener(watcher(passwordLayout));
    }

    private TextWatcher watcher(final TextInputLayout wrapper) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    setError(wrapper, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    @Override
    protected void initView(View view) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        provinceAdapter = ProvinceAdapter.createInstance(getActivity());
        spinnerProvince.setAdapter(provinceAdapter);
        regencyAdapter = RegencyAdapter.createInstance(getActivity());
        spinnerRegency.setAdapter(regencyAdapter);
        subDistrictAdapter = SubDistrictAdapter.createInstance(getActivity());
        spinnerSubDistrict.setAdapter(subDistrictAdapter);
        if (getArguments().getBoolean(IS_EDIT, false)) {
            passwordLayout.setVisibility(View.VISIBLE);
        } else {
            passwordLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setViewListener() {
        chooseLocation.setOnClickListener(onChooseLocation());

        locationEditText.setOnClickListener(onChooseLocation());

        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (pos != 0)
                    provinceError.setVisibility(View.GONE);
                if (!isEdit)
                    presenter.onProvinceSelected(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerRegency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (pos != 0)
                    regencyError.setVisibility(View.GONE);
                if (!isEdit)
                    presenter.onRegencySelected(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerSubDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (pos != 0)
                    subDistrictError.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.saveAddress();
            }
        });

    }

    private View.OnClickListener onChooseLocation() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGeoLocation();
            }
        };
    }

    private void openGeoLocation() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();

        int resultCode = availability.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == resultCode) {
            CommonUtils.dumper("Google play services available");
            LocationPass locationPass = null;
            if (presenter.getLatLng() != null) {
                locationPass = new LocationPass();
                locationPass.setLatitude(String.valueOf(presenter.getLatLng().latitude));
                locationPass.setLongitude(String.valueOf(presenter.getLatLng().longitude));
                locationPass.setGeneratedAddress(locationEditText.getText().toString());
            } else {
                locationPass = new LocationPass();
                locationPass.setCityName((String)spinnerRegency.getSelectedItem());
                locationPass.setDistrictName((String)spinnerSubDistrict.getSelectedItem());
            }
            Intent intent = GeolocationActivity.createInstance(getActivity(), locationPass);
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            CommonUtils.dumper("Google play services unavailable");
            Dialog dialog = availability.getErrorDialog(getActivity(), resultCode, 0);
            dialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            removeError();
            Bundle bundle = data.getExtras();
            LocationPass locationPass = bundle.getParcelable(GeolocationActivity.EXTRA_EXISTING_LOCATION);
            String generatedAddress = locationEditText.getText().toString();
            if (locationPass != null) {
                presenter.setLatLng(locationPass.getLatitude(), locationPass.getLongitude());
                if (locationPass.getGeneratedAddress().equals(getString(R.string.choose_this_location))) {
                    generatedAddress = presenter.getLatLng().latitude + ", " + presenter.getLatLng().longitude;
                } else {
                    generatedAddress = locationPass.getGeneratedAddress();
                }
            }
            locationEditText.setText(generatedAddress);
        }
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }


    @Override
    public void removeError() {

    }

    @Override
    public EditText getReceiverName() {
        return receiverNameEditText;
    }

    @Override
    public EditText getReceiverPhone() {
        return receiverPhoneEditText;
    }

    @Override
    public EditText getPostCode() {
        return postcodeEditText;
    }

    @Override
    public EditText getAddressType() {
        return addressTypeEditText;
    }

    @Override
    public EditText getAddress() {
        return addressEditText;
    }

    @Override
    public Spinner getSpinnerProvince() {
        return spinnerProvince;
    }

    @Override
    public Spinner getSpinnerRegency() {
        return spinnerRegency;
    }

    @Override
    public Spinner getSpinnerSubDistrict() {
        return spinnerSubDistrict;
    }

    @Override
    public void setError(TextInputLayout wrapper, String errorMessage) {
        wrapper.setError(errorMessage);
        if (errorMessage == null) wrapper.setErrorEnabled(false);
        wrapper.requestFocus();
    }

    @Override
    public TextInputLayout getReceiverNameLayout() {
        return receiverNameLayout;
    }

    @Override
    public TextInputLayout getAddressLayout() {
        return addressLayout;
    }

    @Override
    public TextInputLayout getAddressTypeLayout() {
        return addressTypeLayout;
    }

    @Override
    public TextInputLayout getPostCodeLayout() {
        return postCodeLayout;
    }

    @Override
    public TextInputLayout getReceiverPhoneLayout() {
        return receiverPhoneLayout;
    }

    @Override
    public TextView getSpinnerProvinceError() {
        return provinceError;
    }

    @Override
    public TextView getSpinnerRegencyError() {
        return regencyError;
    }

    @Override
    public TextView getSpinnerSubDistrictError() {
        return subDistrictError;
    }

    @Override
    public boolean isEdit() {
        return getArguments().getBoolean(IS_EDIT, false);
    }

    @Override
    public void setProvince(List<Province> provinces) {
        finishLoading();
        provinceAdapter.setList(provinces);
        Destination addressModel = (Destination) getArguments().getParcelable(EDIT_PARAM);
        if (isEdit && addressModel != null) {
            spinnerProvince.setSelection(provinceAdapter.getPositionFromName(addressModel.getProvinceName()));
            presenter.getListCity(provinceAdapter.getList().get(spinnerProvince.getSelectedItemPosition() - 1));
        }
        this.mProvinces = new ArrayList<>(provinces);
    }

    @Override
    public void finishLoading() {
        mProgressDialog.dismiss();
    }

    @Override
    public void finishActivity(Destination address) {
        Intent intent = getActivity().getIntent();
        intent.putExtra(EXTRA_ADDRESS, address);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void showErrorSnackbar(String errorMessage) {
        if (errorMessage.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void showErrorSnackbar(String message, View.OnClickListener listener) {
        if (message.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            SnackbarManager.make(getActivity(), message, Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.title_retry), listener).show();
    }

    @Override
    public void setActionsEnabled(boolean isEnabled) {
        saveButton.setEnabled(isEnabled);
        addressEditText.setEnabled(isEnabled);
        receiverNameEditText.setEnabled(isEnabled);
        addressTypeEditText.setEnabled(isEnabled);
        spinnerProvince.setEnabled(isEnabled);
        spinnerRegency.setEnabled(isEnabled);
        spinnerSubDistrict.setEnabled(isEnabled);
        postcodeEditText.setEnabled(isEnabled);
        receiverPhoneEditText.setEnabled(isEnabled);
        if (isEnabled) {
            chooseLocation.setOnClickListener(onChooseLocation());
            locationEditText.setOnClickListener(onChooseLocation());
        } else {
            chooseLocation.setOnClickListener(null);
            locationEditText.setOnClickListener(null);
        }
    }

    @Override
    public EditText getPassword() {
        return password;
    }

    @Override
    public TextInputLayout getPasswordLayout() {
        return passwordLayout;
    }

    @Override
    public void setResult(Destination address) {
//        getActivity().setResult(address);
    }

    @Override
    public void setDistrict(List<District> districts) {
        progressDistrict.setVisibility(View.GONE);
        districtTitle.setVisibility(View.VISIBLE);
        spinnerSubDistrict.setVisibility(View.VISIBLE);
        subDistrictAdapter.setList(districts);
        Destination addressModel = (Destination) getArguments().getParcelable(EDIT_PARAM);
        if (isEdit && addressModel != null) {
            spinnerSubDistrict.setSelection(subDistrictAdapter.getPositionFromName(addressModel.getDistrictName()));
            setEditParam(addressModel);
            if (subDistrictAdapter.getPositionFromName(addressModel.getDistrictName()) > 0)
                isEdit = false;
        }
        this.mDistricts = new ArrayList<>(districts);
    }

    @Override
    public void setCity(List<City> cities) {
        progressRegency.setVisibility(View.GONE);
        regencyTitle.setVisibility(View.VISIBLE);
        spinnerRegency.setVisibility(View.VISIBLE);
        regencyAdapter.setList(cities);
        Destination addressModel = (Destination) getArguments().getParcelable(EDIT_PARAM);
        if (isEdit && addressModel != null) {
            spinnerRegency.setSelection(regencyAdapter.getPositionFromName(addressModel.getCityName()));
            presenter.getListDistrict(regencyAdapter.getList().get(spinnerRegency.getSelectedItemPosition() - 1));
        }
        this.mCities = new ArrayList<>(cities);
    }

    @Override

    public void resetRegency() {
        regencyAdapter.clearData();
        spinnerRegency.setSelection(0);
    }

    @Override
    public ProvinceAdapter getProvinceAdapter() {
        return provinceAdapter;
    }

    @Override
    public void showLoadingRegency() {
        progressRegency.setVisibility(View.VISIBLE);
        regencyTitle.setVisibility(View.GONE);
        spinnerRegency.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingDistrict() {
        progressDistrict.setVisibility(View.VISIBLE);
        districtTitle.setVisibility(View.GONE);
        spinnerSubDistrict.setVisibility(View.GONE);
    }

    @Override
    public void resetSubDistrict() {
        subDistrictAdapter.clearData();
        spinnerSubDistrict.setSelection(0);
    }

    @Override
    public RegencyAdapter getRegencyAdapter() {
        return regencyAdapter;
    }

    @Override
    public void showLoading() {
        mProgressDialog.showDialog();
    }

    @Override
    public void hideSubDistrict() {
        spinnerSubDistrict.setVisibility(View.GONE);
        districtTitle.setVisibility(View.GONE);
        subDistrictError.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }
}
