package com.tokopedia.logisticaddaddress.features.addaddress;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.logisticaddaddress.data.AddressRepository;
import com.tokopedia.logisticdata.data.entity.address.Destination;
import com.tokopedia.logisticdata.data.module.qualifier.AddressScope;
import com.tokopedia.logisticdata.data.utils.GeoLocationUtils;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by nisie on 9/6/16.
 */
@AddressScope
public class AddAddressPresenterImpl implements AddAddressContract.Presenter {

    private static final String PARAM_ADDRESS_ID = "address_id";
    private static final String PARAM_ADDRESS_TYPE = "address_name";
    private static final String PARAM_ADDRESS = "address_street";
    private static final String PARAM_CITY = "city";
    private static final String PARAM_DISTRICT = "district";
    private static final String PARAM_PROVINCE = "province";
    private static final String PARAM_POSTAL_CODE = "postal_code";
    private static final String PARAM_RECEIVER_NAME = "receiver_name";
    private static final String PARAM_RECEIVER_PHONE = "receiver_phone";
    private static final String PARAM_LATITUDE = "latitude";
    private static final String PARAM_LONGITUDE = "longitude";
    private static final String PARAM_PASSWORD = "user_password";

    private AddAddressContract.View mView;
    private final AddressRepository networkInteractor;
    private UserSessionInterface userSession;

    @Inject
    public AddAddressPresenterImpl(UserSessionInterface userSession, AddressRepository addressRepository) {
        this.networkInteractor = addressRepository;
        this.userSession = userSession;
    }

    @Override
    public void attachView(AddAddressContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        networkInteractor.unsubscribe();
        mView = null;
    }

    @Override
    public void saveAddress() {
        mView.showLoading();
        Map<String, String> param = AuthUtil.generateParamsNetwork(
                userSession.getUserId(), userSession.getDeviceId(), getParam()
        );
        if (mView.isEdit()) {
            networkInteractor.editAddress(mView.context(), param, getListener(true));
        } else {
            networkInteractor.addAddress(mView.context(), param, getListener(false));
        }
    }

    @Override
    public void requestReverseGeoCode(Context context, Destination destination) {
        GeoLocationUtils.getReverseGeoCodeParallel(context,
                Double.parseDouble(destination.getLatitude()),
                Double.parseDouble(destination.getLongitude()), new GeoLocationUtils.GeoLocationListener() {
                    @Override
                    public void getGeoCode(String resultAddress) {
                        mView.setPinpointAddress(resultAddress);
                    }

                    @Override
                    public void onError(String message) {
                        mView.showErrorSnackbar(message);
                    }
                });
    }

    private AddressRepository.AddAddressListener getListener(boolean isEditOperation) {
        return new AddressRepository.AddAddressListener() {
            @Override
            public void onSuccess(String address_id) {
                mView.finishLoading();
                if (!TextUtils.isEmpty(address_id)) {
                    Destination address = mView.getAddress();
                    address.setAddressId(address_id);
                    mView.setAddress(address);
                }
                mView.successSaveAddress();
                if (!isEditOperation) mView.onAddAddressSubmitSuccessRendered();
                mView.finishActivity();
            }

            @Override
            public void onTimeout() {
                mView.finishLoading();
                mView.errorSaveAddress();
                mView.showErrorSnackbar("");
            }

            @Override
            public void onError(String error) {
                mView.finishLoading();
                mView.errorSaveAddress();
                mView.showErrorSnackbar(error);
            }

            @Override
            public void onNullData() {
                mView.finishLoading();
                mView.errorSaveAddress();
                mView.showErrorSnackbar("");
            }

            @Override
            public void onNoNetworkConnection() {
                mView.finishLoading();
                mView.errorSaveAddress();
                mView.showErrorSnackbar("");
            }
        };
    }

    private TKPDMapParam<String, String> getParam() {
        TKPDMapParam<String, String> params;
        Destination address = mView.getAddress();

        if (mView.isEdit()) {
            String password = mView.getPassword();
            params = getParamEditAddress(address, password);
        } else {
            params = getParamAddAddress(address);
        }

        return params;
    }

    private TKPDMapParam<String, String> getParamAddAddress(Destination address) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(PARAM_ADDRESS, (address.getAddressStreet() != null) ? address.getAddressStreet() : "");
        param.put(PARAM_ADDRESS_TYPE, (address.getAddressName() != null) ? address.getAddressName() : "");
        param.put(PARAM_CITY, (address.getCityId() != null) ? address.getCityId() : "");
        param.put(PARAM_DISTRICT, (address.getDistrictId() != null) ? address.getDistrictId() : "");
        param.put(PARAM_PROVINCE, (address.getProvinceId() != null) ? address.getProvinceId() : "");
        param.put(PARAM_POSTAL_CODE, (address.getPostalCode() != null) ? address.getPostalCode() : "");
        param.put(PARAM_RECEIVER_NAME, (address.getReceiverName() != null) ? address.getReceiverName() : "");
        param.put(PARAM_RECEIVER_PHONE, (address.getReceiverPhone() != null) ? address.getReceiverPhone() : "");
        if (address.getLatitude() != null && address.getLongitude() != null) {
            param.put(PARAM_LATITUDE, String.valueOf(address.getLatitude()));
            param.put(PARAM_LONGITUDE, String.valueOf(address.getLongitude()));
        }
        return param;
    }

    private TKPDMapParam<String, String> getParamEditAddress(Destination address, String password) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(PARAM_ADDRESS_ID, (address.getAddressId() != null) ? address.getAddressId() : "");
        param.put(PARAM_ADDRESS, (address.getAddressStreet() != null) ? address.getAddressStreet() : "");
        param.put(PARAM_ADDRESS_TYPE, (address.getAddressName() != null) ? address.getAddressName() : "");
        param.put(PARAM_CITY, (address.getCityId() != null) ? address.getCityId() : "");
        param.put(PARAM_DISTRICT, (address.getDistrictId() != null) ? address.getDistrictId() : "");
        param.put(PARAM_PROVINCE, (address.getProvinceId() != null) ? address.getProvinceId() : "");
        param.put(PARAM_POSTAL_CODE, (address.getPostalCode() != null) ? address.getPostalCode() : "");
        param.put(PARAM_RECEIVER_NAME, (address.getReceiverName() != null) ? address.getReceiverName() : "");
        param.put(PARAM_RECEIVER_PHONE, (address.getReceiverPhone() != null) ? address.getReceiverPhone() : "");
        if (address.getLatitude() != null && address.getLongitude() != null) {
            param.put(PARAM_LATITUDE, String.valueOf(address.getLatitude()));
            param.put(PARAM_LONGITUDE, String.valueOf(address.getLongitude()));
        }
        param.put(PARAM_PASSWORD, password);
        return param;
    }

}

