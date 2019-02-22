package com.tokopedia.logisticaddaddress.features.addaddress;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.logisticaddaddress.R;
import com.tokopedia.logisticaddaddress.di.AddressModule;
import com.tokopedia.logisticaddaddress.di.DaggerAddressComponent;
import com.tokopedia.logisticaddaddress.router.IAddressRouter;
import com.tokopedia.logisticdata.data.entity.address.Destination;
import com.tokopedia.logisticdata.data.entity.address.DistrictRecommendationAddress;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticUserSessionQualifier;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics;
import com.tokopedia.transactionanalytics.listener.ITransactionAnalyticsAddAddress;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static com.tokopedia.logisticaddaddress.AddressConstants.EDIT_PARAM;
import static com.tokopedia.logisticaddaddress.AddressConstants.EXTRA_ADDRESS;
import static com.tokopedia.logisticaddaddress.AddressConstants.EXTRA_FROM_CART_IS_EMPTY_ADDRESS_FIRST;
import static com.tokopedia.logisticaddaddress.AddressConstants.EXTRA_PLATFORM_PAGE;
import static com.tokopedia.logisticaddaddress.AddressConstants.IS_DISTRICT_RECOMMENDATION;
import static com.tokopedia.logisticaddaddress.AddressConstants.IS_EDIT;
import static com.tokopedia.logisticaddaddress.AddressConstants.KERO_TOKEN;
import static com.tokopedia.logisticaddaddress.AddressConstants.PLATFORM_MARKETPLACE_CART;
import static com.tokopedia.logisticaddaddress.AddressConstants.REQUEST_CODE;

/**
 * Created by nisie on 9/6/16.
 */
public class AddAddressFragment extends BaseDaggerFragment
        implements AddAddressContract.View, ITransactionAnalyticsAddAddress {

    public static final int ERROR_RESULT_CODE = 999;
    private static final String EXTRA_EXISTING_LOCATION = "EXTRA_EXISTING_LOCATION";
    private static final int DISTRICT_RECOMMENDATION_REQUEST_CODE = 418;
    private static final String ADDRESS = "district_recommendation_address";
    private static final double MONAS_LATITUDE = -6.175794;
    private static final double MONAS_LONGITUDE = 106.826457;
    private static final int ADDRESS_MAX_CHARACTER = 175;
    private static final int ADDRESS_MIN_CHARACTER = 20;
    private static final String ADDRESS_WATCHER_STRING = "%1$d karakter lagi diperlukan";
    private static final String ADDRESS_WATCHER_STRING2 = "%1$d karakter tersisa";

    private static final String FIREBASE_PERFORMANCE_MONITORING_TRACE_MP_SUBMIT_ADD_ADDRESS = "mp_submit_add_address";

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
    private TextView saveButton;
    private TextView addressLabel;
    private TextInputLayout districtLayout;
    private EditText districtEditText;
    private TextInputLayout zipCodeLayout;
    private AutoCompleteTextView zipCodeTextView;
    private TextInputLayout postCodeLayout;
    private EditText postCodeEditText;
    private Spinner spinnerProvince;
    private TextView provinceError;
    private Spinner spinnerRegency;
    private TextView regencyError;
    private Spinner spinnerSubDistrict;
    private TextView subDistrictError;
    private ProgressBar mProgressBar;

    private List<String> zipCodes;
    private Token token;
    private Destination address;

    private CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;
    private String extraPlatformPage;
    private boolean isFromMarketPlaceCartEmptyAddressFirst;

    @Inject AddAddressContract.Presenter mPresenter;
    @Inject @LogisticUserSessionQualifier UserSessionInterface userSession;
    @Inject PerformanceMonitoring performanceMonitoring;

    public static AddAddressFragment newInstance(Bundle extras) {
        Bundle bundle = new Bundle(extras);
        AddAddressFragment fragment = new AddAddressFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public AddAddressFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            this.token = arguments.getParcelable(KERO_TOKEN);
            this.address = arguments.getParcelable(EDIT_PARAM);
            this.extraPlatformPage = arguments.getString(EXTRA_PLATFORM_PAGE, "");
            this.isFromMarketPlaceCartEmptyAddressFirst = arguments.getBoolean(EXTRA_FROM_CART_IS_EMPTY_ADDRESS_FIRST, false);
        }

        if (token == null) {
            getActivity().setResult(ERROR_RESULT_CODE);
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.logistic_fragment_add_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter.attachView(this);
        initView(view);
        initialVar();
        setViewListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        sendAnalyticsScreenName(getScreenName());
    }

    @Override
    public void onResume() {
        super.onResume();
        setTextWatcher();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                String generatedAddress = locationEditText.getText().toString();

                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    LocationPass locationPass = bundle.getParcelable(EXTRA_EXISTING_LOCATION);
                    if (locationPass != null) {
                        String latitude = locationPass.getLatitude();
                        String longitude = locationPass.getLongitude();

                        if (!latitude.isEmpty() && !longitude.isEmpty()) {
                            address.setLatLng(latitude, longitude);
                        } else {
                            address.setLatLng(String.valueOf(MONAS_LATITUDE), String.valueOf(MONAS_LONGITUDE));
                        }

                        if (locationPass.getGeneratedAddress().equals(getString(R.string.choose_this_location))) {
                            generatedAddress = address.getLatitude() + ", " + address.getLongitude();
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
    protected void initInjector() {
        BaseAppComponent appComponent = ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent();
        DaggerAddressComponent.builder()
                .baseAppComponent(appComponent)
                .addressModule(new AddressModule())
                .build().inject(this);
    }

    @Override
    public void setPinpointAddress(String address) {
        locationEditText.setText(address);
    }

    @Override
    public void stopPerformaceMonitoring() {
        performanceMonitoring.stopTrace();
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
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void finishActivity() {
        Intent intent = getActivity().getIntent();
        intent.putExtra(EXTRA_ADDRESS, address);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void showErrorSnackbar(String message) {
        if (getActivity() == null) return;
        if (message == null || TextUtils.isEmpty(message)) {
            ToasterError.make(BaseToaster.getContentView(getActivity()),
                    getActivity().getResources().getString(R.string.msg_network_error),
                    BaseToaster.LENGTH_SHORT).show();
        } else {
            ToasterError.make(BaseToaster.getContentView(getActivity()),
                    message, BaseToaster.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isValidAddress() {
        boolean isValid = true;

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
        if (addressLength < 20) {
            String errorMessage = getString(R.string.error_min_address);

            if (addressLength == 0) {
                errorMessage = getString(R.string.error_field_required);
            }

            addressLayout.setError(errorMessage);
            addressLabel.setVisibility(View.GONE);
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
    public void errorSaveAddress() {
        sendAnalyticsOnSaveAddressButtonWithoutErrorValidation(false);
    }

    @Override
    public void successSaveAddress() {
        sendAnalyticsOnSaveAddressButtonWithoutErrorValidation(true);
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
        checkoutAnalyticsChangeAddress.sendScreenName(getActivity(), screenName);
    }

    @Override
    protected String getScreenName() {
        if (isAddAddressFromCartCheckoutMarketplace()) {
            return isFromMarketPlaceCartEmptyAddressFirst()
                    ? ConstantTransactionAnalytics.ScreenName.ADD_NEW_ADDRESS_PAGE_FROM_EMPTY_ADDRESS_CART
                    : ConstantTransactionAnalytics.ScreenName.ADD_NEW_ADDRESS_PAGE;
        }
        return ConstantTransactionAnalytics.ScreenName.ADD_NEW_ADDRESS_PAGE_USER;
    }

    public void setError(TextInputLayout wrapper, String errorMessage) {
        wrapper.setError(errorMessage);
        if (errorMessage == null) wrapper.setErrorEnabled(false);
        wrapper.requestFocus();
    }

    public void initializeZipCodes() {
        zipCodeTextView.setText("");
        String header = getResources().getString(R.string.hint_type_postal_code);
        if (!zipCodes.contains(header)) zipCodes.add(0, header);

        ArrayAdapter<String> zipCodeAdapter = new ArrayAdapter<>(
                getContext(),
                R.layout.item_autocomplete_text_double_row,
                R.id.item,
                zipCodes);

        zipCodeTextView.setAdapter(zipCodeAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
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
                performanceMonitoring.startTrace(FIREBASE_PERFORMANCE_MONITORING_TRACE_MP_SUBMIT_ADD_ADDRESS);
                mPresenter.saveAddress();
            }
        });
    }

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
        saveButton = view.findViewById(R.id.save_button);
        addressLabel = view.findViewById(R.id.address_label_watcher);

        districtLayout = view.findViewById(R.id.district_layout);
        districtEditText = view.findViewById(R.id.district);
        zipCodeLayout = view.findViewById(R.id.postal_code_layout);
        zipCodeTextView = view.findViewById(R.id.autocomplete_postal_code);

        postCodeLayout = view.findViewById(R.id.post_code_layout);
        postCodeEditText = view.findViewById(R.id.post_code);
        spinnerProvince = view.findViewById(R.id.provinsi);
        provinceError = view.findViewById(R.id.province_error);
        spinnerRegency = view.findViewById(R.id.regency);
        regencyError = view.findViewById(R.id.regency_error);
        spinnerSubDistrict = view.findViewById(R.id.sub_district);
        subDistrictError = view.findViewById(R.id.sub_district_error);

        if (!isEdit() && isFromMarketPlaceCartEmptyAddressFirst()) {
            addressTypeEditText.setText(getResources().getString(R.string.address_type_default));
            receiverNameEditText.setText(userSession.getName());
            receiverPhoneEditText.setText(userSession.getPhoneNumber());
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mProgressBar = view.findViewById(R.id.logistic_spinner);
    }

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
            requestPinpointAddress(address);
        } else if (address == null) {
            address = new Destination();
        }
    }

    private void setTextWatcher() {
        receiverNameEditText.addTextChangedListener(watcher(receiverNameLayout));
        addressEditText.addTextChangedListener(watcher(addressLayout));
        addressEditText.addTextChangedListener(characterWatcher(addressLabel));
        addressTypeEditText.addTextChangedListener(watcher(addressTypeLayout));
        receiverPhoneEditText.addTextChangedListener(watcher(receiverPhoneLayout));

        districtEditText.addTextChangedListener(watcher(districtLayout));
        zipCodeTextView.addTextChangedListener(watcher(zipCodeLayout));

        postCodeEditText.addTextChangedListener(watcher(postCodeLayout));

    }

    private TextWatcher characterWatcher(TextView watcher) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (watcher.getVisibility() != View.VISIBLE) watcher.setVisibility(View.VISIBLE);
                int textLength = charSequence.length();
                int charLeft;
                if (textLength < ADDRESS_MIN_CHARACTER) {
                    charLeft = ADDRESS_MIN_CHARACTER - textLength;
                    watcher.setText(String.format(Locale.US, ADDRESS_WATCHER_STRING, charLeft));
                } else {
                    charLeft = ADDRESS_MAX_CHARACTER - textLength;
                    watcher.setText(String.format(Locale.US, ADDRESS_WATCHER_STRING2, charLeft));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
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
                    zipCodeLayout.setError(getContext().getString(R.string.error_field_required));
                } else {
                    zipCodeLayout.setError(null);
                    sendAnalyticsOnZipCodeInputFreeText(s.toString());
                }
            }
        };
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
        return view -> {
            sendAnalyticsOnDistrictSelectionClicked();
            Intent intent = ((IAddressRouter) getActivity().getApplication())
                    .getDistrictRecommendationIntent(
                            getActivity(), token,
                            getArguments().getString(EXTRA_PLATFORM_PAGE, "").equalsIgnoreCase(PLATFORM_MARKETPLACE_CART)
                    );
            startActivityForResult(intent, DISTRICT_RECOMMENDATION_REQUEST_CODE);
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

    private void requestPinpointAddress(Destination address) {
        if (address.getLatitude() != null &&
                address.getLongitude() != null &&
                !address.getLatitude().equals("") &&
                !address.getLongitude().equals("")
                ) {
            mPresenter.requestReverseGeoCode(getContext(), address);
        }
    }

    private View.OnClickListener onChooseLocation() {
        return view -> {
            openGeoLocation();
            sendAnalyticsOnLocationSelectionClicked();
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

            Intent intent = ((IAddressRouter) getActivity().getApplication())
                    .getGeoLocationActivityIntent(
                            getActivity(), locationPass,
                            isAddAddressFromCartCheckoutMarketplace()
                    );
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            CommonUtils.dumper("Google play services unavailable");
            Dialog dialog = availability.getErrorDialog(getActivity(), resultCode, 0);
            dialog.show();
        }
    }

    private boolean isAddAddressFromCartCheckoutMarketplace() {
        return extraPlatformPage.equalsIgnoreCase(PLATFORM_MARKETPLACE_CART);
    }

    private boolean isFromMarketPlaceCartEmptyAddressFirst() {
        return isFromMarketPlaceCartEmptyAddressFirst;
    }
}
