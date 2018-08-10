package com.tokopedia.core.manage.people.address.presenter;

import android.text.TextUtils;

import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.manage.people.address.interactor.AddAddressRetrofitInteractor;
import com.tokopedia.core.manage.people.address.interactor.AddAddressRetrofitInteractorImpl;
import com.tokopedia.core.manage.people.address.listener.AddAddressFragmentView;
import com.tokopedia.core.manage.people.address.model.Destination;
import com.tokopedia.core.manage.people.address.model.FormAddressDomainModel;

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
            networkInteractor.editAddress(viewListener.context(), getParam(), getListener());
        } else {
            networkInteractor.addAddress(viewListener.context(), getParam(), getListener());
        }
    }

    @Override
    public void getListProvince() {
        viewListener.setActionsEnabled(false);
        viewListener.showLoading();
        networkInteractor.getListProvince(viewListener.context(),
                new HashMap<String, String>(),
                new AddAddressRetrofitInteractor.GetListProvinceListener() {
                    @Override
                    public void onSuccess(ArrayList<Province> provinces) {
                        viewListener.setActionsEnabled(true);
                        viewListener.setProvince(provinces);
                    }

                    @Override
                    public void onTimeout() {
                        viewListener.finishLoading();
                        viewListener.showErrorSnackbar("");
                    }

                    @Override
                    public void onError(String error) {
                        viewListener.finishLoading();
                        viewListener.setActionsEnabled(true);
                        viewListener.showErrorSnackbar(error);
                    }

                    @Override
                    public void onNullData() {
                        viewListener.finishLoading();
                        viewListener.showErrorSnackbar("");
                    }

                });
    }

    @Override
    public void onProvinceSelected(int pos) {
        viewListener.resetRegency();
        viewListener.hideSubDistrict();
        viewListener.resetSubDistrict();
        if (pos != 0) {
            getListCity(viewListener.getProvinceAdapter().getList().get(pos - 1));
        }
    }

    @Override
    public void onEditProvinceSelected(int pos) {
        viewListener.resetRegency();
        viewListener.hideSubDistrict();
        viewListener.resetSubDistrict();
        if (pos != 0) {
            provinceChanged(viewListener.getProvinceAdapter().getList().get(pos - 1));
        }
    }

    @Override
    public void onRegencySelected(int pos) {
        viewListener.resetSubDistrict();
        if (pos != 0) {
            getListDistrict(viewListener.getRegencyAdapter().getList().get(pos - 1));
        }
    }

    @Override
    public void getListCity(Province province) {
        viewListener.showLoadingRegency();
        viewListener.setActionsEnabled(false);
        networkInteractor.getListCity(viewListener.context(),
                province.getProvinceId(),
                new AddAddressRetrofitInteractor.GetListCityListener() {

                    @Override
                    public void onSuccess(FormAddressDomainModel model) {
                        viewListener.setActionsEnabled(true);
                        viewListener.setCity(model.getCities());
                    }

                    @Override
                    public void onTimeout() {
                        viewListener.finishLoading();
                        viewListener.showErrorSnackbar("");
                    }

                    @Override
                    public void onError(String error) {
                        viewListener.finishLoading();
                        viewListener.setActionsEnabled(true);
                        viewListener.showErrorSnackbar(error);
                    }

                    @Override
                    public void onNullData() {
                        viewListener.finishLoading();
                        viewListener.showErrorSnackbar("");
                    }

                });
    }

    @Override
    public void provinceChanged(Province province) {
        viewListener.showLoadingRegency();
        viewListener.setActionsEnabled(false);
        networkInteractor.getListCity(
                viewListener.context(),
                province.getProvinceId(),
                new AddAddressRetrofitInteractor.GetListCityListener() {
                    @Override
                    public void onSuccess(FormAddressDomainModel model) {
                        viewListener.setActionsEnabled(true);
                        viewListener.changeProvince(model.getCities());
                    }

                    @Override
                    public void onTimeout() {
                        viewListener.finishLoading();
                        viewListener.showErrorSnackbar("");
                    }

                    @Override
                    public void onError(String error) {
                        viewListener.finishLoading();
                        viewListener.setActionsEnabled(true);
                        viewListener.showErrorSnackbar(error);
                    }

                    @Override
                    public void onNullData() {
                        viewListener.finishLoading();
                        viewListener.showErrorSnackbar("");
                    }
                });
    }

    @Override
    public void getListDistrict(City city) {
        viewListener.showLoadingDistrict();
        viewListener.setActionsEnabled(false);
        networkInteractor.getListDistrict(viewListener.context(), city.getCityId(), new AddAddressRetrofitInteractor.GetListDistrictListener() {
            @Override
            public void onSuccess(FormAddressDomainModel model) {
                viewListener.setActionsEnabled(true);
                viewListener.setDistrict(model.getDistricts());
            }

            @Override
            public void onTimeout() {
                viewListener.finishLoading();
                viewListener.showErrorSnackbar("");
            }

            @Override
            public void onError(String error) {
                viewListener.finishLoading();
                viewListener.setActionsEnabled(true);
                viewListener.showErrorSnackbar(error);
            }

            @Override
            public void onNullData() {
                viewListener.finishLoading();
                viewListener.showErrorSnackbar("");
            }
        });
    }

    private AddAddressRetrofitInteractor.AddAddressListener getListener() {
        return new AddAddressRetrofitInteractor.AddAddressListener() {
            @Override
            public void onSuccess(String address_id) {
                viewListener.finishLoading();
                if (!TextUtils.isEmpty(address_id)) {
                    Destination address = viewListener.getAddress();
                    address.setAddressId(address_id);
                    viewListener.setAddress(address);
                }
                viewListener.successSaveAddress();
                viewListener.finishActivity();
            }

            @Override
            public void onTimeout() {
                viewListener.finishLoading();
                viewListener.errorSaveAddress();
                viewListener.showErrorSnackbar("");
            }

            @Override
            public void onError(String error) {
                viewListener.finishLoading();
                viewListener.errorSaveAddress();
                viewListener.showErrorSnackbar(error);
            }

            @Override
            public void onNullData() {
                viewListener.finishLoading();
                viewListener.errorSaveAddress();
                viewListener.showErrorSnackbar("");
            }

            @Override
            public void onNoNetworkConnection() {
                viewListener.finishLoading();
                viewListener.errorSaveAddress();
                viewListener.showErrorSnackbar("");
            }
        };
    }

    private Map<String, String> getParam() {
        Map<String, String> params;
        Destination address = viewListener.getAddress();

        if (viewListener.isEdit()) {
            String password = viewListener.getPassword();
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

