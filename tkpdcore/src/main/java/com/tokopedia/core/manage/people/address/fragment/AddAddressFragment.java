package com.tokopedia.core.manage.people.address.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.core2.R;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.District;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import com.tokopedia.core.manage.people.address.fragment.adapter.ProvinceAdapter;
import com.tokopedia.core.manage.people.address.fragment.adapter.RegencyAdapter;
import com.tokopedia.core.manage.people.address.fragment.adapter.SubDistrictAdapter;
import com.tokopedia.core.manage.people.address.listener.AddAddressFragmentView;
import com.tokopedia.core.manage.people.address.model.Destination;
import com.tokopedia.core.manage.people.address.model.DistrictRecommendationAddress;
import com.tokopedia.core.manage.people.address.model.Token;
import com.tokopedia.core.manage.people.address.presenter.AddAddressPresenter;
import com.tokopedia.core.manage.people.address.presenter.AddAddressPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics;
import com.tokopedia.transactionanalytics.listener.ITransactionAnalyticsAddAddress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tokopedia.core.manage.people.address.ManageAddressConstant.EDIT_PARAM;
import static com.tokopedia.core.manage.people.address.ManageAddressConstant.EXTRA_ADDRESS;
import static com.tokopedia.core.manage.people.address.ManageAddressConstant.EXTRA_FROM_CART_IS_EMPTY_ADDRESS_FIRST;
import static com.tokopedia.core.manage.people.address.ManageAddressConstant.EXTRA_PLATFORM_PAGE;
import static com.tokopedia.core.manage.people.address.ManageAddressConstant.IS_DISTRICT_RECOMMENDATION;
import static com.tokopedia.core.manage.people.address.ManageAddressConstant.IS_EDIT;
import static com.tokopedia.core.manage.people.address.ManageAddressConstant.KERO_TOKEN;
import static com.tokopedia.core.manage.people.address.ManageAddressConstant.PLATFORM_MARKETPLACE_CART;
import static com.tokopedia.core.manage.people.address.ManageAddressConstant.REQUEST_CODE;

/**
 * Created by nisie on 9/6/16.
 */
public class AddAddressFragment extends BasePresenterFragment<AddAddressPresenter>
        implements AddAddressFragmentView, ITransactionAnalyticsAddAddress, GeoLocationUtils.GeoLocationListener {

    private static final int DISTRICT_RECOMMENDATION_REQUEST_CODE = 130715;
    private static final String ADDRESS = "district_recommendation_address";

    private static final double MONAS_LATITUDE = -6.175794;
    private static final double MONAS_LONGITUDE = 106.826457;

    private TextInputLayout receiverNameLayout;
    private EditText receiverNameEditText;
    private TextInputLayout addressTypeLayout;
    private EditText addressTypeEditText;
    private TextInputLayout addressLayout;
    private EditText addressEditText;
    private TextInputLayout receiverPhoneLayout;
    private EditText receiverPhoneEditText;
    private View chooseLocation;
    private EditText locationEditText;
    private TextInputLayout passwordLayout;
    private EditText passwordEditText;
    private TextView saveButton;

    private TextInputLayout districtLayout;
    private EditText districtEditText;
    private TextInputLayout zipCodeLayout;
    private AutoCompleteTextView zipCodeTextView;

    private TextInputLayout postCodeLayout;
    private EditText postCodeEditText;
    private LinearLayout addressSpinerLayout;
    private Spinner spinnerProvince;
    private TextView provinceError;
    private TextView regencyTitle;
    private ProgressBar progressRegency;
    private Spinner spinnerRegency;
    private TextView regencyError;
    private TextView districtTitle;
    private ProgressBar progressDistrict;
    private Spinner spinnerSubDistrict;
    private TextView subDistrictError;

    private List<String> zipCodes;
    private Token token;

    private Destination address;

    ProvinceAdapter provinceAdapter;
    RegencyAdapter regencyAdapter;
    SubDistrictAdapter subDistrictAdapter;

    List<Province> mProvinces;
    List<City> mCities;
    List<District> mDistricts;

    TkpdProgressDialog mProgressDialog;
    private CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;

    private String extraPlatformPage;
    private boolean isFromMarketPlaceCartEmptyAddressFirst;

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
        if (!isDistrictRecommendation()) presenter.getListProvince();
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

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
        this.token = arguments.getParcelable(KERO_TOKEN);
        this.address = arguments.getParcelable(EDIT_PARAM);
        this.extraPlatformPage = arguments.getString(EXTRA_PLATFORM_PAGE, "");
        this.isFromMarketPlaceCartEmptyAddressFirst = arguments.getBoolean(EXTRA_FROM_CART_IS_EMPTY_ADDRESS_FIRST, false);
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
        receiverPhoneEditText.addTextChangedListener(watcher(receiverPhoneLayout));
        passwordEditText.addTextChangedListener(watcher(passwordLayout));

        districtEditText.addTextChangedListener(watcher(districtLayout));
        zipCodeTextView.addTextChangedListener(watcher(zipCodeLayout));

        postCodeEditText.addTextChangedListener(watcher(postCodeLayout));

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

    private TextWatcher zipPostTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (zipCodeTextView.getText().length() < 1) {
                    zipCodeLayout.setError(context.getString(R.string.error_field_required));
                } else {
                    zipCodeLayout.setError(null);
                    sendAnalyticsOnZipCodeInputFreeText(s.toString());
                }
            }
        };
    }

    @Override
    protected void initView(View view) {
        receiverNameLayout = view.findViewById(R.id.receiver_name_layout);
        receiverNameEditText = view.findViewById(R.id.receiver_name);
        addressTypeLayout = view.findViewById(R.id.address_type_layout);
        addressTypeEditText = view.findViewById(R.id.address_type);
        addressLayout = view.findViewById(R.id.address_layout);
        addressEditText = view.findViewById(R.id.address);
        receiverPhoneLayout = view.findViewById(R.id.receiver_phone_layout);
        receiverPhoneEditText = view.findViewById(R.id.receiver_phone);
        chooseLocation = view.findViewById(R.id.layout_value_location);
        locationEditText = view.findViewById(R.id.value_location);
        passwordLayout = view.findViewById(R.id.password_layout);
        passwordEditText = view.findViewById(R.id.password);
        saveButton = view.findViewById(R.id.save_button);

        districtLayout = view.findViewById(R.id.district_layout);
        districtEditText = view.findViewById(R.id.district);
        zipCodeLayout = view.findViewById(R.id.postal_code_layout);
        zipCodeTextView = view.findViewById(R.id.autocomplete_postal_code);

        postCodeLayout = view.findViewById(R.id.post_code_layout);
        postCodeEditText = view.findViewById(R.id.post_code);
        addressSpinerLayout = view.findViewById(R.id.address_spinner_layout);
        spinnerProvince = view.findViewById(R.id.provinsi);
        provinceError = view.findViewById(R.id.province_error);
        regencyTitle = view.findViewById(R.id.regency_title);
        progressRegency = view.findViewById(R.id.regency_progress);
        spinnerRegency = view.findViewById(R.id.regency);
        regencyError = view.findViewById(R.id.regency_error);
        districtTitle = view.findViewById(R.id.district_title);
        progressDistrict = view.findViewById(R.id.district_progress);
        spinnerSubDistrict = view.findViewById(R.id.sub_district);
        subDistrictError = view.findViewById(R.id.sub_district_error);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        provinceAdapter = ProvinceAdapter.createInstance(getActivity());
        spinnerProvince.setAdapter(provinceAdapter);
        regencyAdapter = RegencyAdapter.createInstance(getActivity());
        spinnerRegency.setAdapter(regencyAdapter);
        subDistrictAdapter = SubDistrictAdapter.createInstance(getActivity());
        spinnerSubDistrict.setAdapter(subDistrictAdapter);

        passwordLayout.setVisibility(isEdit() ? View.VISIBLE : View.GONE);
        selectLayout();
    }

    private void selectLayout() {
        // TODO ATTENTION: when new checkout flow is fully released, please refactor (remove) this part immediately
        if (isDistrictRecommendation() && token != null) {
            addressSpinerLayout.setVisibility(View.GONE);
            postCodeLayout.setVisibility(View.GONE);
            districtLayout.setVisibility(View.VISIBLE);
            zipCodeLayout.setVisibility(View.VISIBLE);
        } else {
            addressSpinerLayout.setVisibility(View.VISIBLE);
            postCodeLayout.setVisibility(View.VISIBLE);
            districtLayout.setVisibility(View.GONE);
            zipCodeLayout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void setViewListener() {
        zipCodeLayout.setOnTouchListener(onZipCodeTouch());
        zipCodeTextView.setOnTouchListener(onZipCodeTouch());
        zipCodeTextView.setOnItemClickListener(onZipCodeItemClick());
        zipCodeTextView.addTextChangedListener(zipPostTextWatcher());
        districtLayout.setOnClickListener(onCityDistrictClick());
        districtEditText.setOnClickListener(onCityDistrictClick());
        chooseLocation.setOnClickListener(onChooseLocation());
        locationEditText.setOnClickListener(onChooseLocation());

        addressTypeEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP)
                sendAnalyticsOnInputAddressAsClicked();
            return false;
        });

        receiverNameEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP)
                sendAnalyticsOnInputNameClicked();
            return false;
        });

        addressEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP)
                sendAnalyticsOnInputAddressClicked();
            return false;
        });

        receiverPhoneEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP)
                sendAnalyticsOnInputPhoneClicked();
            return false;
        });


        saveButton.setOnClickListener(view -> {
            sendAnalyticsOnSubmitSaveAddressClicked();
            if (isValidAddress()) {
                updateAddress();
                presenter.saveAddress();
            }
        });

        spinnerProvince.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                        if (pos != 0) {
                            provinceError.setVisibility(View.GONE);

                            List<Province> provinceList = provinceAdapter.getList();
                            Province province = provinceList.get(pos - 1);
                            address.setProvinceName(province.getProvinceName());
                            address.setProvinceId(province.getProvinceId());
                        }

                        if (isEdit()) presenter.onEditProvinceSelected(pos);
                        else presenter.onProvinceSelected(pos);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );

        spinnerRegency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (pos != 0) {
                    regencyError.setVisibility(View.GONE);

                    List<City> cityList = regencyAdapter.getList();
                    City city = cityList.get(pos - 1);
                    address.setCityName(city.getCityName());
                    address.setCityId(city.getCityId());
                    presenter.onRegencySelected(pos);
                }

                if (!isEdit()) presenter.onRegencySelected(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerSubDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (pos != 0) {
                    subDistrictError.setVisibility(View.GONE);

                    List<District> districtList = subDistrictAdapter.getList();
                    District district = districtList.get(pos - 1);
                    address.setDistrictName(district.getDistrictName());
                    address.setDistrictId(district.getDistrictId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateAddress() {
        if (address == null) address = new Destination();

        if (isDistrictRecommendation()) {
            address.setPostalCode(zipCodeTextView.getText().toString());
        } else {
            address.setPostalCode(postCodeEditText.getText().toString());
        }

        address.setAddressName(addressTypeEditText.getText().toString());
        address.setReceiverName(receiverNameEditText.getText().toString());
        address.setAddressStreet(addressEditText.getText().toString());
        address.setReceiverPhone(receiverPhoneEditText.getText().toString());
    }

    private View.OnClickListener onCityDistrictClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Moved to module router, this class isn't supposed to be used anymore
                /*sendAnalyticsOnDistrictSelectionClicked();
                Intent intent = ((TkpdCoreRouter) getActivity().getApplication())
                        .getDistrictRecommendationIntent(
                                getActivity(), token,
                                getArguments().getString(EXTRA_PLATFORM_PAGE, "").equalsIgnoreCase(PLATFORM_MARKETPLACE_CART)
                        );
                startActivityForResult(intent, DISTRICT_RECOMMENDATION_REQUEST_CODE);*/
            }
        };
    }

    @SuppressLint("ClickableViewAccessibility")
    private View.OnTouchListener onZipCodeTouch() {
        return (view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                sendAnalyticsOnZipCodeSelectionClicked();
            if (!zipCodeTextView.isPopupShowing()) zipCodeTextView.showDropDown();
            return false;
        };
    }

    private AdapterView.OnItemClickListener onZipCodeItemClick() {
        return (adapterView, view, i, l) -> {
            if (i == 0 && !Character.isDigit(zipCodeTextView.getText().toString().charAt(0))) {
                zipCodeTextView.setText("");
            }

            address.setPostalCode(zipCodeTextView.getText().toString());
            sendAnalyticsOnZipCodeDropdownSelectionClicked();
        };
    }

    private void setPinpointAddress(Destination address) {
        if (address.getLatitude() != null &&
                address.getLongitude() != null &&
                !address.getLatitude().equals("") &&
                !address.getLongitude().equals("")
                ) {
            GeoLocationUtils.reverseGeoCodeParallel(
                    getActivity(),
                    address.getLatitude(),
                    address.getLongitude(), this
            );
        }
    }

    public void initializeZipCodes() {
        zipCodeTextView.setText("");
        String header = getResources().getString(R.string.hint_type_postal_code);
        if (!zipCodes.contains(header)) zipCodes.add(0, header);

        ArrayAdapter<String> zipCodeAdapter = new ArrayAdapter<>(
                context,
                R.layout.item_autocomplete_text_double_row,
                R.id.item,
                zipCodes);

        zipCodeTextView.setAdapter(zipCodeAdapter);
    }

    private View.OnClickListener onChooseLocation() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGeoLocation();
                sendAnalyticsOnLocationSelectionClicked();
            }
        };
    }

    private void openGeoLocation() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();

        int resultCode = availability.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == resultCode) {
            CommonUtils.dumper("Google play services available");

            LocationPass locationPass = new LocationPass();

            if (!TextUtils.isEmpty(address.getLatitude())
                    && !TextUtils.isEmpty(address.getLongitude())
                    && !address.getLatitude().equals(String.valueOf(MONAS_LATITUDE))
                    && !address.getLongitude().equals(String.valueOf(MONAS_LONGITUDE))) {
                locationPass.setLatitude(address.getLatitude());
                locationPass.setLongitude(address.getLongitude());
                locationPass.setGeneratedAddress(locationEditText.getText().toString());
            } else if (!TextUtils.isEmpty(address.getCityName()) && !TextUtils.isEmpty(address.getDistrictName())) {
                locationPass.setDistrictName(address.getDistrictName());
                locationPass.setCityName(address.getCityName());
            } else {
                locationPass.setLatitude(String.valueOf(MONAS_LATITUDE));
                locationPass.setLongitude(String.valueOf(MONAS_LONGITUDE));
            }
            if (getArguments().getString(EXTRA_PLATFORM_PAGE, "")
                    .equalsIgnoreCase(PLATFORM_MARKETPLACE_CART)) {
                startActivityForResult(GeolocationActivity.createInstanceFromMarketplaceCart(
                        getActivity(), locationPass), REQUEST_CODE
                );
            } else {
                startActivityForResult(GeolocationActivity.createInstanceIntent(
                        getActivity(), locationPass), REQUEST_CODE
                );
            }
        } else {
            CommonUtils.dumper("Google play services unavailable");
            Dialog dialog = availability.getErrorDialog(getActivity(), resultCode, 0);
            dialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                String generatedAddress = locationEditText.getText().toString();

                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    LocationPass locationPass = bundle.getParcelable(GeolocationActivity.EXTRA_EXISTING_LOCATION);
                    if (locationPass != null) {
                        String latitude = locationPass.getLatitude();
                        String longitude = locationPass.getLongitude();

                        if (!latitude.isEmpty() && !longitude.isEmpty()) {
                            address.setLatLng(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
                        } else {
                            address.setLatLng(new LatLng(MONAS_LATITUDE, MONAS_LONGITUDE));
                        }

                        if (locationPass.getGeneratedAddress().equals(getString(R.string.choose_this_location))) {
                            generatedAddress = address.getLatLng().latitude + ", " + address.getLatLng().longitude;
                        } else {
                            generatedAddress = locationPass.getGeneratedAddress();
                        }
                    }
                }

                locationEditText.setText(generatedAddress);

            } else if (requestCode == DISTRICT_RECOMMENDATION_REQUEST_CODE) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    DistrictRecommendationAddress address = bundle.getParcelable(ADDRESS);
                    if (address != null) {
                        List<String> compositeAddress = new ArrayList<>(Arrays.asList(
                                address.getDistrictName(),
                                address.getCityName(),
                                address.getProvinceName()
                        ));
                        String fullAddress = TextUtils.join(", ", compositeAddress);
                        districtEditText.setText(fullAddress);

                        this.address.setProvinceId(String.valueOf(address.getProvinceId()));
                        this.address.setCityId(String.valueOf(address.getCityId()));
                        this.address.setDistrictId(String.valueOf(address.getDistrictId()));
                        this.address.setProvinceName(address.getProvinceName());
                        this.address.setCityName(address.getCityName());
                        this.address.setDistrictName(address.getDistrictName());

                        zipCodes = new ArrayList<>(address.getZipCodes());
                        initializeZipCodes();
                    }
                }
            }
        }
    }

    @Override
    protected void initialVar() {
        if (getActivity().getApplication() instanceof AbstractionRouter) {
            AnalyticTracker analyticTracker = ((AbstractionRouter) getActivity().getApplication()).getAnalyticTracker();
            checkoutAnalyticsChangeAddress = new CheckoutAnalyticsChangeAddress(analyticTracker);
        }
        if (isEdit() && address != null) {
            receiverNameEditText.setText(address.getReceiverName());
            addressTypeEditText.setText(address.getAddressName());
            addressEditText.setText(address.getAddressStreet());
            districtEditText.setText(TextUtils.join(", ", Arrays.asList(
                    address.getProvinceName(),
                    address.getCityName(),
                    address.getDistrictName()
            )));

            zipCodeTextView.setText(address.getPostalCode());
            postCodeEditText.setText(address.getPostalCode());
            receiverPhoneEditText.setText(address.getReceiverPhone());
            setPinpointAddress(address);
        } else if (address == null) {
            address = new Destination();
        }
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public Context context() {
        return getActivity();
    }

    @Override
    public boolean isEdit() {
        return getArguments().getBoolean(IS_EDIT, false);
    }

    @Override
    public boolean isDistrictRecommendation() {
        return getArguments().getBoolean(IS_DISTRICT_RECOMMENDATION, false);
    }

    @Override
    public void finishLoading() {
        mProgressDialog.dismiss();
    }

    @Override
    public void finishActivity() {
        Intent intent = getActivity().getIntent();
        intent.putExtra(EXTRA_ADDRESS, address);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    public void setError(TextInputLayout wrapper, String errorMessage) {
        wrapper.setError(errorMessage);
        if (errorMessage == null) wrapper.setErrorEnabled(false);
        wrapper.requestFocus();
    }

    @Override
    public void showErrorSnackbar(String message) {
        if (TextUtils.isEmpty(message)) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), message);
        }
    }

    @Override
    public void setActionsEnabled(boolean isEnabled) {
        saveButton.setEnabled(isEnabled);
        addressEditText.setEnabled(isEnabled);
        receiverNameEditText.setEnabled(isEnabled);
        addressTypeEditText.setEnabled(isEnabled);
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
    public String getPassword() {
        return passwordEditText.getText().toString();
    }

    @Override
    public void showLoading() {
        mProgressDialog.showDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        if (isAddAddressFromCartCheckoutMarketplace()) {
            return isFromMarketPlaceCartEmptyAddressFirst()
                    ? ConstantTransactionAnalytics.ScreenName.ADD_NEW_ADDRESS_PAGE_FROM_EMPTY_ADDRESS_CART
                    : ConstantTransactionAnalytics.ScreenName.ADD_NEW_ADDRESS_PAGE;
        }
        return super.getScreenName();
    }

    @Override
    public void onStart() {
        super.onStart();
        sendAnalyticsScreenName(getScreenName());
    }

    private boolean isAddAddressFromCartCheckoutMarketplace() {
        return extraPlatformPage.equalsIgnoreCase(PLATFORM_MARKETPLACE_CART);
    }

    private boolean isFromMarketPlaceCartEmptyAddressFirst() {
        return isFromMarketPlaceCartEmptyAddressFirst;
    }

    public boolean isValidAddress() {
        boolean isValid = true;

        // Check password validity
        if (isEdit() && getPassword().length() == 0) {
            String errorMessage = getString(R.string.error_field_required);
            passwordLayout.setError(errorMessage);
            passwordLayout.requestFocus();
            isValid = false;
            sendAnalyticsOnValidationErrorSaveAddress(errorMessage);
        }

        // Check receiver phone validity
        int phoneLength = receiverPhoneEditText.getText().length();
        if (phoneLength < 6 || phoneLength > 20) {
            String errorMessage = getString(R.string.error_min_phone_numer);

            if (phoneLength > 20) {
                errorMessage = getString(R.string.error_max_phone_numer);
            } else if (phoneLength == 0) {
                errorMessage = getString(R.string.error_field_required);
            }

            receiverPhoneLayout.setError(errorMessage);
            receiverPhoneLayout.requestFocus();
            sendAnalyticsOnValidationErrorSaveAddress(errorMessage);
            sendAnalyticsOnErrorInputPhone();
            isValid = false;
        }

        // Check address validity
        int addressLength = addressEditText.getText().length();
        if (addressLength <= 20) {
            String errorMessage = getString(R.string.error_min_address);

            if (addressLength == 0) {
                errorMessage = getString(R.string.error_field_required);
            }

            addressLayout.setError(errorMessage);
            addressLayout.requestFocus();
            sendAnalyticsOnValidationErrorSaveAddress(errorMessage);
            sendAnalyticsOnErrorInputAddress();
            isValid = false;
        }

        // Check receiver name validity
        int receiverNameLength = receiverNameEditText.getText().length();
        if (receiverNameLength == 0 || receiverNameLength > 128) {
            String errorMessage = getString(R.string.error_field_required);
            if (receiverNameLength > 128) {
                errorMessage = getString(R.string.error_max_128_character);
            }

            receiverNameLayout.setError(errorMessage);
            receiverNameLayout.requestFocus();
            sendAnalyticsOnValidationErrorSaveAddress(errorMessage);
            sendAnalyticsOnErrorInputName();
            isValid = false;
        }

        if (TextUtils.isEmpty(addressTypeEditText.getText())) {
            String errorMessage = getString(R.string.error_field_required);
            addressTypeLayout.setError(errorMessage);
            addressTypeLayout.requestFocus();
            sendAnalyticsOnValidationErrorSaveAddress(errorMessage);
            sendAnalyticsOnErrorInputAddressAs();
            isValid = false;
        }

        if (isDistrictRecommendation()) {
            if (TextUtils.isEmpty(districtEditText.getText())) {
                String errorMessage = getString(R.string.error_field_required);
                districtLayout.setError(errorMessage);
                districtLayout.requestFocus();
                sendAnalyticsOnValidationErrorSaveAddress(errorMessage);
                sendAnalyticsOnErrorInputDistrict();
                isValid = false;
            }

            if (TextUtils.isEmpty(zipCodeTextView.getText())) {
                String errorMessage = getString(R.string.error_field_required);
                zipCodeLayout.setError(errorMessage);
                zipCodeLayout.requestFocus();
                sendAnalyticsOnValidationErrorSaveAddress(errorMessage);
                sendAnalyticsOnErrorInputZipCode();
                isValid = false;
            }
        } else {
            if (postCodeEditText.getText().length() == 0) {
                String errorMessage = getString(R.string.error_field_required);
                postCodeLayout.setError(errorMessage);
                postCodeLayout.requestFocus();
                sendAnalyticsOnValidationErrorSaveAddress(errorMessage);
                sendAnalyticsOnErrorInputZipCode();
                isValid = false;
            } else if (postCodeEditText.getText().length() < 5) {
                String errorMessage = getString(R.string.error_min_5_character);
                postCodeLayout.setError(errorMessage);
                postCodeLayout.requestFocus();
                sendAnalyticsOnValidationErrorSaveAddress(errorMessage);
                sendAnalyticsOnErrorInputZipCode();
                isValid = false;
            } else if (postCodeEditText.getText().length() > 10) {
                String errorMessage = getString(R.string.error_max_post_code);
                postCodeLayout.setError(errorMessage);
                postCodeLayout.requestFocus();
                sendAnalyticsOnValidationErrorSaveAddress(errorMessage);
                sendAnalyticsOnErrorInputZipCode();
                isValid = false;
            }

            if (spinnerProvince.getSelectedItemPosition() == 0) {
                provinceError.setVisibility(View.VISIBLE);
                isValid = false;
            }
            if (spinnerProvince.getSelectedItemPosition() != 0 &&
                    spinnerRegency.getSelectedItemPosition() == 0) {
                regencyError.setVisibility(View.VISIBLE);
                isValid = false;
            }
            if (spinnerProvince.getSelectedItemPosition() != 0 &&
                    spinnerRegency.getSelectedItemPosition() != 0 &&
                    spinnerSubDistrict.getSelectedItemPosition() == 0) {
                subDistrictError.setVisibility(View.VISIBLE);
                isValid = false;
            }
        }

        return isValid;
    }

    @Override
    public Destination getAddress() {
        return this.address;
    }

    @Override
    public void setAddress(Destination address) {
        this.address = address;
    }

    @Override
    public void setProvince(List<Province> provinces) {
        finishLoading();
        provinceAdapter.setList(provinces);
        Destination addressModel = getArguments().getParcelable(EDIT_PARAM);
        if (isEdit() && addressModel != null) {
            spinnerProvince.setSelection(provinceAdapter.getPositionFromName(addressModel.getProvinceName()));
            presenter.getListCity(provinceAdapter.getList().get(spinnerProvince.getSelectedItemPosition() - 1));
        }
        this.mProvinces = new ArrayList<>(provinces);
    }

    @Override
    public void resetRegency() {
        regencyAdapter.clearData();
        spinnerRegency.setSelection(0);
    }

    @Override
    public void hideSubDistrict() {
        spinnerSubDistrict.setVisibility(View.GONE);
        districtTitle.setVisibility(View.GONE);
        subDistrictError.setVisibility(View.GONE);
    }

    @Override
    public void resetSubDistrict() {
        subDistrictAdapter.clearData();
        spinnerSubDistrict.setSelection(0);
    }

    @Override
    public ProvinceAdapter getProvinceAdapter() {
        return provinceAdapter;
    }

    @Override
    public RegencyAdapter getRegencyAdapter() {
        return regencyAdapter;
    }

    @Override
    public void showLoadingRegency() {
        progressRegency.setVisibility(View.VISIBLE);
        regencyTitle.setVisibility(View.GONE);
        spinnerRegency.setVisibility(View.GONE);
    }

    @Override
    public void setCity(List<City> cities) {
        progressRegency.setVisibility(View.GONE);
        regencyTitle.setVisibility(View.VISIBLE);
        spinnerRegency.setVisibility(View.VISIBLE);
        regencyAdapter.setList(cities);
        Destination addressModel = getArguments().getParcelable(EDIT_PARAM);
        if (isEdit() && addressModel != null) {
            spinnerRegency.setSelection(regencyAdapter.getPositionFromName(addressModel.getCityName()));
            presenter.getListDistrict(regencyAdapter.getList().get(spinnerRegency.getSelectedItemPosition() - 1));
        }
        this.mCities = new ArrayList<>(cities);
    }

    @Override
    public void changeProvince(List<City> cities) {
        progressRegency.setVisibility(View.GONE);
        regencyTitle.setVisibility(View.VISIBLE);
        spinnerRegency.setVisibility(View.VISIBLE);
        regencyAdapter.setList(cities);
        Destination addressModel = getArguments().getParcelable(EDIT_PARAM);
        if (addressModel != null)
            spinnerRegency
                    .setSelection(regencyAdapter.getPositionFromName(addressModel.getCityName()));
        this.mCities = new ArrayList<>(cities);
    }

    @Override
    public void showLoadingDistrict() {
        progressDistrict.setVisibility(View.VISIBLE);
        districtTitle.setVisibility(View.GONE);
        spinnerSubDistrict.setVisibility(View.GONE);
    }

    @Override
    public void setDistrict(List<District> districts) {
        progressDistrict.setVisibility(View.GONE);
        districtTitle.setVisibility(View.VISIBLE);
        spinnerSubDistrict.setVisibility(View.VISIBLE);
        subDistrictAdapter.setList(districts);
        Destination addressModel = getArguments().getParcelable(EDIT_PARAM);
        if (isEdit() && addressModel != null) {
            spinnerSubDistrict.setSelection(subDistrictAdapter.getPositionFromName(addressModel.getDistrictName()));
            updateAddress();
        }
        this.mDistricts = new ArrayList<>(districts);
    }

    @Override
    public void errorSaveAddress() {
        sendAnalyticsOnSaveAddressButtonWithoutErrorValidation(false);
    }

    @Override
    public void successSaveAddress() {
        sendAnalyticsOnSaveAddressButtonWithoutErrorValidation(true);
    }

    @Override
    public void getGeoCode(String resultAddress) {
        locationEditText.setText(resultAddress);
    }

    @Override
    public void sendAnalyticsOnSubmitSaveAddressClicked() {
        if (isAddAddressFromCartCheckoutMarketplace()) {
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickSimpanFromTambahAlamat();
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickTambahAlamatFromTambah();
        }
    }

    @Override
    public void sendAnalyticsOnDistrictSelectionClicked() {
        if (isAddAddressFromCartCheckoutMarketplace()) {
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickKotaAtauKecamatanPadaTambahAddress();
        }
    }

    @Override
    public void sendAnalyticsOnZipCodeSelectionClicked() {
        if (isAddAddressFromCartCheckoutMarketplace()) {
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickKodePosPadaTambahAddress();
        }
    }

    @Override
    public void sendAnalyticsOnLocationSelectionClicked() {
        if (isAddAddressFromCartCheckoutMarketplace()) {
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickTandaiLokasiPadaTambahAddress();
        }
    }

    @Override
    public void sendAnalyticsOnZipCodeDropdownSelectionClicked() {
        if (isAddAddressFromCartCheckoutMarketplace()) {
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickChecklistKodePosPAdaTambahAddress();
        }
    }

    @Override
    public void sendAnalyticsOnZipCodeInputFreeText(String zipCode) {
        if (isAddAddressFromCartCheckoutMarketplace()) {
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickFillKodePosPadaTambahAddress(zipCode);
        }
    }

    @Override
    public void sendAnalyticsOnValidationErrorSaveAddress(String errorMessageValidation) {
        if (isAddAddressFromCartCheckoutMarketplace()) {
            checkoutAnalyticsChangeAddress.eventViewShippingCartChangeAddressViewValidationErrorNotFill(errorMessageValidation);
        }
    }

    @Override
    public void sendAnalyticsOnInputAddressAsClicked() {
        if (isAddAddressFromCartCheckoutMarketplace())
            checkoutAnalyticsChangeAddress.eventClickCourierCartChangeAddressClickAlamatSebagaiPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnInputAddressAsDropdownSelectionItemCliked() {
        if (isAddAddressFromCartCheckoutMarketplace())
            checkoutAnalyticsChangeAddress.eventClickCourierCartChangeAddressClickChecklistAlamatSebagaiPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnInputNameClicked() {
        if (isAddAddressFromCartCheckoutMarketplace())
            checkoutAnalyticsChangeAddress.eventClickCourierCartChangeAddressClickNamaPadaTambahAddress();

    }

    @Override
    public void sendAnalyticsOnInputPhoneClicked() {
        if (isAddAddressFromCartCheckoutMarketplace())
            checkoutAnalyticsChangeAddress.eventClickCourierCartChangeAddressClickTeleponPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnInputAddressClicked() {
        if (isAddAddressFromCartCheckoutMarketplace())
            checkoutAnalyticsChangeAddress.eventClickCourierCartChangeAddressClickAlamatPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnErrorInputAddressAs() {
        if (isAddAddressFromCartCheckoutMarketplace())
            checkoutAnalyticsChangeAddress.eventClickCourierCartChangeAddressErrorValidationAlamatSebagaiPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnErrorInputName() {
        if (isAddAddressFromCartCheckoutMarketplace())
            checkoutAnalyticsChangeAddress.eventClickCourierCartChangeAddressErrorValidationNamaPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnErrorInputPhone() {
        if (isAddAddressFromCartCheckoutMarketplace())
            checkoutAnalyticsChangeAddress.eventClickCourierCartChangeAddressErrorValidationTeleponPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnErrorInputDistrict() {
        if (isAddAddressFromCartCheckoutMarketplace())
            checkoutAnalyticsChangeAddress.eventClickCourierCartChangeAddressErrorValidationKotaKecamatanPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnErrorInputZipCode() {
        if (isAddAddressFromCartCheckoutMarketplace())
            checkoutAnalyticsChangeAddress.eventClickCourierCartChangeAddressErrorValidationKodePosPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnErrorInputAddress() {
        if (isAddAddressFromCartCheckoutMarketplace())
            checkoutAnalyticsChangeAddress.eventClickCourierCartChangeAddressErrorValidationAlamatPadaTambahAddress();
    }

    @Override
    public void sendAnalyticsOnSaveAddressButtonWithoutErrorValidation(boolean success) {
        if (isAddAddressFromCartCheckoutMarketplace())
            if (success) {
                checkoutAnalyticsChangeAddress.eventClickAddressCartChangeAddressClickTambahFromTambahAlamatBaruSuccess();
                checkoutAnalyticsChangeAddress.eventClickCourierCartChangeAddressErrorValidationAlamatSebagaiPadaTambahSuccess();
            } else {
                checkoutAnalyticsChangeAddress.eventClickAddressCartChangeAddressClickTambahFromTambahAlamatBaruFailed();
                checkoutAnalyticsChangeAddress.eventClickCourierCartChangeAddressErrorValidationAlamatSebagaiPadaTambahNotSuccess();
            }
    }

    @Override
    public void sendAnalyticsScreenName(String screenName) {
        if (isAddAddressFromCartCheckoutMarketplace())
            checkoutAnalyticsChangeAddress.sendScreenName(getActivity(), screenName);
    }
}
