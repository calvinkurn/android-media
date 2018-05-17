package com.tokopedia.core.manage.people.address.fragment;

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
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.manage.people.address.listener.AddAddressFragmentView;
import com.tokopedia.core.manage.people.address.model.Destination;
import com.tokopedia.core.manage.people.address.model.DistrictRecommendationAddress;
import com.tokopedia.core.manage.people.address.model.Token;
import com.tokopedia.core.manage.people.address.presenter.AddAddressPresenter;
import com.tokopedia.core.manage.people.address.presenter.AddAddressPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tokopedia.core.manage.people.address.ManageAddressConstant.EDIT_PARAM;
import static com.tokopedia.core.manage.people.address.ManageAddressConstant.EXTRA_ADDRESS;
import static com.tokopedia.core.manage.people.address.ManageAddressConstant.IS_EDIT;
import static com.tokopedia.core.manage.people.address.ManageAddressConstant.KERO_TOKEN;
import static com.tokopedia.core.manage.people.address.ManageAddressConstant.REQUEST_CODE;

/**
 * Created by nisie on 9/6/16.
 */
public class AddAddressFragment extends BasePresenterFragment<AddAddressPresenter>
        implements AddAddressFragmentView {

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
    private TextInputLayout districtLayout;
    private EditText districtEditText;
    private TextInputLayout zipCodeLayout;
    private AutoCompleteTextView zipCodeTextView;
    private TextInputLayout receiverPhoneLayout;
    private EditText receiverPhoneEditText;
    private View chooseLocation;
    private EditText locationEditText;
    private TextInputLayout passwordLayout;
    private EditText password;

    private TextView saveButton;

    private List<String> zipCodes;
    private Token token;

    private Destination address;

    TkpdProgressDialog mProgressDialog;

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
        districtEditText.addTextChangedListener(watcher(districtLayout));
        zipCodeTextView.addTextChangedListener(watcher(zipCodeLayout));
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
        districtLayout = view.findViewById(R.id.district_layout);
        districtEditText = view.findViewById(R.id.district);
        zipCodeLayout = view.findViewById(R.id.postal_code_layout);
        zipCodeTextView = view.findViewById(R.id.postal_code);
        receiverPhoneLayout = view.findViewById(R.id.receiver_phone_layout);
        receiverPhoneEditText = view.findViewById(R.id.receiver_phone);
        chooseLocation = view.findViewById(R.id.layout_value_location);
        locationEditText = view.findViewById(R.id.value_location);
        passwordLayout = view.findViewById(R.id.password_layout);
        password = view.findViewById(R.id.password);
        saveButton = view.findViewById(R.id.save_button);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        passwordLayout.setVisibility(isEdit() ? View.VISIBLE : View.GONE);
    }

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
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidAddress()) {
                    updateAddress();
                    presenter.saveAddress();
                }
            }
        });
    }

    private void updateAddress() {
        if (address == null) address = new Destination();

        String[] splitAddress = districtEditText.getText().toString().split(", ");
        address.setProvinceName(splitAddress[0]);
        address.setCityName(splitAddress[1]);
        address.setDistrictName(splitAddress[2]);

        address.setAddressName(addressTypeEditText.getText().toString());
        address.setReceiverName(receiverNameEditText.getText().toString());
        address.setAddressStreet(addressEditText.getText().toString());
        address.setPostalCode(zipCodeTextView.getText().toString());
        address.setReceiverPhone(receiverPhoneEditText.getText().toString());
    }

    private View.OnClickListener onCityDistrictClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ((TkpdCoreRouter) getActivity().getApplication())
                        .getDistrictRecommendationIntent(getActivity(), token);
                startActivityForResult(intent, DISTRICT_RECOMMENDATION_REQUEST_CODE);
            }
        };
    }

    private View.OnTouchListener onZipCodeTouch() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!zipCodeTextView.isPopupShowing()) zipCodeTextView.showDropDown();
                return false;
            }
        };
    }

    private AdapterView.OnItemClickListener onZipCodeItemClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0 && !Character.isDigit(zipCodeTextView.getText().toString().charAt(0))) {
                    zipCodeTextView.setText("");
                }

                address.setPostalCode(zipCodeTextView.getText().toString());
            }
        };
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
            }
        };
    }

    private void openGeoLocation() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();

        int resultCode = availability.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == resultCode) {
            CommonUtils.dumper("Google play services available");

            LocationPass locationPass = new LocationPass();

            if (address.getLatLng() != null) {
                locationPass = new LocationPass();
                locationPass.setLatitude(String.valueOf(address.getLatLng().latitude));
                locationPass.setLongitude(String.valueOf(address.getLatLng().longitude));
                locationPass.setGeneratedAddress(locationEditText.getText().toString());
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

                        this.address.setCityId(String.valueOf(address.getCityId()));
                        this.address.setDistrictId(String.valueOf(address.getDistrictId()));
                        this.address.setProvinceId(String.valueOf(address.getProvinceId()));

                        zipCodes = new ArrayList<>(address.getZipCodes());
                        initializeZipCodes();
                    }
                }
            }
        }
    }

    @Override
    protected void initialVar() {
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
            receiverPhoneEditText.setText(address.getReceiverPhone());
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
        return password.getText().toString();
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

    public boolean isValidAddress() {
        boolean isValid = true;

        // Check password validity
        if (isEdit() && getPassword().length() == 0) {
            passwordLayout.setError(getString(R.string.error_field_required));
            passwordLayout.requestFocus();
            isValid = false;
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
            isValid = false;
        }

        // Check receiver name validity
        int receiverNameLength = receiverNameEditText.getText().length();
        if (receiverNameLength == 0 || receiverNameLength > 128) {
            String errorMessage = getString(R.string.error_field_required);
            if (receiverNameLength > 128) {
                errorMessage = getString(R.string.error_max_128_character);
            }

            receiverNameLayout.setError( errorMessage);
            receiverNameLayout.requestFocus();
            isValid = false;
        }

        if (addressTypeEditText.getText().length() == 0) {
            addressTypeLayout.setError(getString(R.string.error_field_required));
            addressTypeLayout.requestFocus();
            isValid = false;
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
}
