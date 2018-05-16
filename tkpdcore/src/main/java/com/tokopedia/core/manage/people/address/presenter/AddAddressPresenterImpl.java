package com.tokopedia.core.manage.people.address.presenter;

import android.text.TextUtils;

import com.tokopedia.core.manage.people.address.interactor.AddAddressRetrofitInteractor;
import com.tokopedia.core.manage.people.address.interactor.AddAddressRetrofitInteractorImpl;
import com.tokopedia.core.manage.people.address.listener.AddAddressFragmentView;
import com.tokopedia.core.manage.people.address.model.Destination;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nisie on 9/6/16.
 */
public class AddAddressPresenterImpl implements AddAddressPresenter {

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

    private static final double MONAS_LATITUDE = -6.175794;
    private static final double MONAS_LONGITUDE = 106.826457;

    private final AddAddressFragmentView viewListener;
    private final AddAddressRetrofitInteractor networkInteractor;

    public AddAddressPresenterImpl(AddAddressFragmentView viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new AddAddressRetrofitInteractorImpl();
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {
        networkInteractor.unsubscribe();
    }

    @Override
    public void saveAddress() {
        viewListener.showLoading();

        if (viewListener.isEdit()) {
            networkInteractor.editAddress(viewListener.context(),
                    getParam(),
                    getAddAddressListener()
            );
        } else {
            networkInteractor.addAddress(viewListener.context(),
                    getParam(),
                    getAddAddressListener()
            );
        }
    }

    private AddAddressRetrofitInteractor.AddAddressListener getAddAddressListener() {
        return new AddAddressRetrofitInteractor.AddAddressListener() {
            @Override
            public void onSuccess(String address_id) {
                viewListener.finishLoading();
                if (!TextUtils.isEmpty(address_id)) {
                    Destination address = viewListener.getAddress();
                    address.setAddressId(address_id);
                    viewListener.setAddress(address);
                }

                viewListener.finishActivity();
            }

            @Override
            public void onTimeout() {
                viewListener.finishLoading();
                viewListener.showErrorSnackbar("");
            }

            @Override
            public void onError(String error) {
                viewListener.finishLoading();
                viewListener.showErrorSnackbar(error);
            }

            @Override
            public void onNullData() {
                viewListener.finishLoading();
                viewListener.showErrorSnackbar("");
            }

            @Override
            public void onNoNetworkConnection() {
                viewListener.finishLoading();
                viewListener.showErrorSnackbar("");
            }
        };
    }

    private Map<String, String> getParam() {
        Destination address = viewListener.getAddress();
        if (viewListener.isEdit()) {
            String password = viewListener.getPassword();
            return getParamEditAddress(address, password);
        } else {
            return getParamAddAddress(address);
        }
    }

    private HashMap<String, String> getParamAddAddress(Destination address) {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_ADDRESS, address.getAddressStreet());
        param.put(PARAM_ADDRESS_TYPE, address.getAddressName());
        param.put(PARAM_CITY, address.getCityId());
        param.put(PARAM_DISTRICT, address.getDistrictId());
        param.put(PARAM_PROVINCE, address.getProvinceId());
        param.put(PARAM_POSTAL_CODE, address.getPostalCode());
        param.put(PARAM_RECEIVER_NAME, address.getReceiverName());
        param.put(PARAM_RECEIVER_PHONE, address.getReceiverPhone());
        if (address.getLatitude() != null && address.getLongitude() != null) {
            param.put(PARAM_LATITUDE, String.valueOf(address.getLatitude()));
            param.put(PARAM_LONGITUDE, String.valueOf(address.getLongitude()));
        }
        return param;
    }

    private Map<String, String> getParamEditAddress(Destination address, String password) {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_ADDRESS_ID, address.getAddressId());
        param.put(PARAM_ADDRESS, address.getAddressStreet());
        param.put(PARAM_ADDRESS_TYPE, address.getAddressName());
        param.put(PARAM_CITY, address.getCityId());
        param.put(PARAM_DISTRICT, address.getDistrictId());
        param.put(PARAM_PROVINCE, address.getProvinceId());
        param.put(PARAM_POSTAL_CODE, address.getPostalCode());
        param.put(PARAM_RECEIVER_NAME, address.getReceiverName());
        param.put(PARAM_RECEIVER_PHONE, address.getReceiverPhone());
        if (address.getLatitude() != null && address.getLongitude() != null) {
            param.put(PARAM_LATITUDE, String.valueOf(address.getLatitude()));
            param.put(PARAM_LONGITUDE, String.valueOf(address.getLongitude()));
        }
        param.put(PARAM_PASSWORD, password);
        return param;
    }

}

