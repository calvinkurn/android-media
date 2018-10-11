package com.tokopedia.logisticaddaddress.addaddress;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.tokopedia.logisticaddaddress.GeoLocationUtils;
import com.tokopedia.logisticdata.data.apiservice.AddressApi;
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;
import com.tokopedia.logisticdata.data.constant.LogisticDataConstantUrl;
import com.tokopedia.logisticdata.data.entity.address.Destination;
import com.tokopedia.logisticdata.data.entity.address.FormAddressDomainModel;
import com.tokopedia.logisticdata.data.entity.address.db.City;
import com.tokopedia.logisticdata.data.entity.address.db.Province;
import com.tokopedia.logisticaddaddress.network.AddressRepository;
import com.tokopedia.logisticaddaddress.network.AddAddressRetrofitInteractorImpl;
import com.tokopedia.network.CommonNetwork;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.OkHttpClient;

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
    private UserSession userSession;

    public AddAddressPresenterImpl(UserSession userSession) {
        Context context = mView.context();
        NetworkRouter router = ((NetworkRouter) ((Fragment) mView).getActivity().getApplication());
        this.networkInteractor = new AddAddressRetrofitInteractorImpl(
                CommonNetwork.createRetrofit(LogisticDataConstantUrl.PeopleAction.BASE_URL,
                        new TkpdOkHttpBuilder(context, new OkHttpClient.Builder()),
                        new TkpdAuthInterceptor(context, router, userSession),
                        new FingerprintInterceptor(router, userSession),
                        new StringResponseConverter(), new GsonBuilder())
                        .create(PeopleActApi.class),
                CommonNetwork.createRetrofit(TkpdBaseURL.Etc.URL_ADDRESS,
                        new TkpdOkHttpBuilder(context, new OkHttpClient.Builder()),
                        new TkpdAuthInterceptor(context, router, userSession),
                        new FingerprintInterceptor(router, userSession),
                        new StringResponseConverter(), new GsonBuilder())
                        .create(AddressApi.class)
        );
        this.userSession = userSession;
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
        TKPDMapParam<String, String> param = AuthUtil.generateParamsNetwork(
                userSession.getUserId(), userSession.getDeviceId(), getParam()
        );
        if (mView.isEdit()) {
            networkInteractor.editAddress(mView.context(), param, getListener());
        } else {
            networkInteractor.addAddress(mView.context(), param, getListener());
        }
    }

    @Override
    public void getListProvince() {
        mView.setActionsEnabled(false);
        mView.showLoading();
        // todo : test this
        Map<String, String> param =
                AuthUtil.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), new TKPDMapParam<>());
        networkInteractor.getListProvince(mView.context(), param,
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

    @Override
    public void requestReverseGeoCode(Context context, Destination destination) {
        GeoLocationUtils.getReverseGeoCodeParallel(context,
                Double.parseDouble(destination.getLatitude()),
                Double.parseDouble(destination.getLongitude()), resultAddress -> {
                    mView.setPinpointAddress(resultAddress);
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

    private TKPDMapParam<String, String> getParamEditAddress(Destination address, String password) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
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

