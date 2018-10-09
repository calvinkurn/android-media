package com.tokopedia.logisticaddaddress.addaddress;

import android.text.TextUtils;


import com.tokopedia.core.manage.people.address.model.Destination;
import com.tokopedia.logisticaddaddress.model.db.City;
import com.tokopedia.logisticaddaddress.model.db.Province;
import com.tokopedia.logisticaddaddress.AddressRepository;
import com.tokopedia.logisticaddaddress.AddAddressRetrofitInteractorImpl;
import com.tokopedia.logisticaddaddress.model.FormAddressDomainModel;

import java.util.ArrayList;
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

    private AddAddressFragmentView mView;
    private final AddressRepository networkInteractor;

    public AddAddressPresenterImpl() {
        this.networkInteractor = new AddAddressRetrofitInteractorImpl();
    }

    @Override
    public void attachView(AddAddressFragmentView view) {
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
        if (mView.isEdit()) {
            networkInteractor.editAddress(mView.context(), getParam(), getListener());
        } else {
            networkInteractor.addAddress(mView.context(), getParam(), getListener());
        }
    }

    @Override
    public void getListProvince() {
        mView.setActionsEnabled(false);
        mView.showLoading();
        networkInteractor.getListProvince(mView.context(),
                new HashMap<String, String>(),
                new AddressRepository.GetListProvinceListener() {
                    @Override
                    public void onSuccess(ArrayList<Province> provinces) {
                        mView.setActionsEnabled(true);
                        mView.setProvince(provinces);
                    }

                    @Override
                    public void onTimeout() {
                        mView.finishLoading();
                        mView.showErrorSnackbar("");
                    }

                    @Override
                    public void onError(String error) {
                        mView.finishLoading();
                        mView.setActionsEnabled(true);
                        mView.showErrorSnackbar(error);
                    }

                    @Override
                    public void onNullData() {
                        mView.finishLoading();
                        mView.showErrorSnackbar("");
                    }

                });
    }

    @Override
    public void onProvinceSelected(int pos) {
        mView.resetRegency();
        mView.hideSubDistrict();
        mView.resetSubDistrict();
        if (pos != 0) {
            getListCity(mView.getProvinceAdapter().getList().get(pos - 1));
        }
    }

    @Override
    public void onEditProvinceSelected(int pos) {
        mView.resetRegency();
        mView.hideSubDistrict();
        mView.resetSubDistrict();
        if (pos != 0) {
            provinceChanged(mView.getProvinceAdapter().getList().get(pos - 1));
        }
    }

    @Override
    public void onRegencySelected(int pos) {
        mView.resetSubDistrict();
        if (pos != 0) {
            getListDistrict(mView.getRegencyAdapter().getList().get(pos - 1));
        }
    }

    @Override
    public void getListCity(Province province) {
        mView.showLoadingRegency();
        mView.setActionsEnabled(false);
        networkInteractor.getListCity(mView.context(),
                province.getProvinceId(),
                new AddressRepository.GetListCityListener() {

                    @Override
                    public void onSuccess(FormAddressDomainModel model) {
                        mView.setActionsEnabled(true);
                        mView.setCity(model.getCities());
                    }

                    @Override
                    public void onTimeout() {
                        mView.finishLoading();
                        mView.showErrorSnackbar("");
                    }

                    @Override
                    public void onError(String error) {
                        mView.finishLoading();
                        mView.setActionsEnabled(true);
                        mView.showErrorSnackbar(error);
                    }

                    @Override
                    public void onNullData() {
                        mView.finishLoading();
                        mView.showErrorSnackbar("");
                    }

                });
    }

    @Override
    public void provinceChanged(Province province) {
        mView.showLoadingRegency();
        mView.setActionsEnabled(false);
        networkInteractor.getListCity(
                mView.context(),
                province.getProvinceId(),
                new AddressRepository.GetListCityListener() {
                    @Override
                    public void onSuccess(FormAddressDomainModel model) {
                        mView.setActionsEnabled(true);
                        mView.changeProvince(model.getCities());
                    }

                    @Override
                    public void onTimeout() {
                        mView.finishLoading();
                        mView.showErrorSnackbar("");
                    }

                    @Override
                    public void onError(String error) {
                        mView.finishLoading();
                        mView.setActionsEnabled(true);
                        mView.showErrorSnackbar(error);
                    }

                    @Override
                    public void onNullData() {
                        mView.finishLoading();
                        mView.showErrorSnackbar("");
                    }
                });
    }

    @Override
    public void getListDistrict(City city) {
        mView.showLoadingDistrict();
        mView.setActionsEnabled(false);
        networkInteractor.getListDistrict(mView.context(), city.getCityId(), new AddressRepository.GetListDistrictListener() {
            @Override
            public void onSuccess(FormAddressDomainModel model) {
                mView.setActionsEnabled(true);
                mView.setDistrict(model.getDistricts());
            }

            @Override
            public void onTimeout() {
                mView.finishLoading();
                mView.showErrorSnackbar("");
            }

            @Override
            public void onError(String error) {
                mView.finishLoading();
                mView.setActionsEnabled(true);
                mView.showErrorSnackbar(error);
            }

            @Override
            public void onNullData() {
                mView.finishLoading();
                mView.showErrorSnackbar("");
            }
        });
    }

    private AddressRepository.AddAddressListener getListener() {
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

    private Map<String, String> getParam() {
        Map<String, String> params;
        Destination address = mView.getAddress();

        if (mView.isEdit()) {
            String password = mView.getPassword();
            params = getParamEditAddress(address, password);
        } else {
            params = getParamAddAddress(address);
        }

        return params;
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

