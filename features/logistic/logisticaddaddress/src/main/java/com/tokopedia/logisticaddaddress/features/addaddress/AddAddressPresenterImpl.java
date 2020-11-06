package com.tokopedia.logisticaddaddress.features.addaddress;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.logisticaddaddress.data.AddressRepository;
import com.tokopedia.logisticdata.data.entity.address.Destination;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.logisticdata.data.entity.response.KeroMapsAutofill;
import com.tokopedia.logisticdata.data.module.qualifier.AddressScope;
import com.tokopedia.logisticdata.domain.param.EditAddressParam;
import com.tokopedia.logisticdata.domain.usecase.EditAddressUseCase;
import com.tokopedia.logisticdata.domain.usecase.RevGeocodeUseCase;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

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
    private static final String DEFAULT_ERROR_MESSAGE = "Terjadi kesalahan pada server. Ulangi beberapa saat lagi";
    private static final double MONAS_LATITUDE = -6.175794;
    private static final double MONAS_LONGITUDE = 106.826457;

    private AddAddressContract.View mView;
    private AddressRepository networkInteractor;
    private UserSessionInterface userSession;
    private RevGeocodeUseCase revGeocodeUseCase;
    private EditAddressUseCase editAddressUseCase;

    @Inject
    public AddAddressPresenterImpl(UserSessionInterface userSession,
                                   AddressRepository addressRepository,
                                   RevGeocodeUseCase revGeocodeUseCase,
                                   EditAddressUseCase editAddressUseCase) {
        this.networkInteractor = addressRepository;
        this.userSession = userSession;
        this.revGeocodeUseCase = revGeocodeUseCase;
        this.editAddressUseCase = editAddressUseCase;
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
        Map<String, String> param = AuthHelper.generateParamsNetwork(
                userSession.getUserId(), userSession.getDeviceId(), getParam()
        );
        if (mView.isEdit()) {
            networkInteractor.editAddress(param, getListener(true));
        } else {
            networkInteractor.addAddress(param, getListener(false));
        }
    }

    @Override
    public void requestReverseGeoCode(Context context, Destination destination) {
        String keyword = String.format("%s,%s", destination.getLatitude(), destination.getLongitude());
        revGeocodeUseCase.execute(keyword)
                .subscribe(new Subscriber<KeroMapsAutofill>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showErrorSnackbar(e.getMessage());
                    }

                    @Override
                    public void onNext(KeroMapsAutofill keroMapsAutofill) {
                        mView.setPinpointAddress(
                                keroMapsAutofill.getData().getFormattedAddress());
                    }
                });
    }

    @Override
    public void editAddressPinPoint(Destination address, String locationText) {
        LocationPass locationPass = new LocationPass();
        RequestParams requestParams = generatePinpointRequest(address);
        editAddressUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showErrorSnackbar(e.getMessage());
                    }

                    @Override
                    public void onNext(String stringResponse) {
                        JSONObject response = null;
                        String messageError = null;
                        boolean statusSuccess;
                        try {
                            response = new JSONObject(stringResponse);
                            int statusCode = response.getJSONObject(EditAddressUseCase.RESPONSE_DATA)
                                    .getInt(EditAddressUseCase.RESPONSE_IS_SUCCESS);
                            statusSuccess = statusCode == 1;
                            if (!statusSuccess) {
                                messageError = response.getJSONArray("message_error").getString(0);
                            }
                        } catch (JSONException e) {
                            Timber.d(e);
                            statusSuccess = false;
                        }

                        if (response != null && statusSuccess) {
                            if (!TextUtils.isEmpty(address.getLatitude())
                                    && !TextUtils.isEmpty(address.getLongitude())
                                    && !address.getLatitude().equals(String.valueOf(MONAS_LATITUDE))
                                    && !address.getLongitude().equals(String.valueOf(MONAS_LONGITUDE))) {
                                locationPass.setLatitude(address.getLatitude());
                                locationPass.setLongitude(address.getLongitude());
                                locationPass.setGeneratedAddress(locationText);
                            } else if (!TextUtils.isEmpty(address.getCityName()) && !TextUtils.isEmpty(address.getDistrictName())) {
                                locationPass.setDistrictName(address.getDistrictName());
                                locationPass.setCityName(address.getCityName());
                            } else {
                                locationPass.setLatitude(String.valueOf(MONAS_LATITUDE));
                                locationPass.setLongitude(String.valueOf(MONAS_LONGITUDE));
                            }
                            mView.goToGeolocationActivity(locationPass);
                        } else {
                            if (messageError.isEmpty()) {
                                messageError = DEFAULT_ERROR_MESSAGE;
                            }
                            mView.showErrorSnackbar(messageError);
                        }
                    }
                });

    }

    private RequestParams generatePinpointRequest(Destination address) {
        Map<String, String> params = AuthHelper.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), new TKPDMapParam<>());
        params.put(EditAddressParam.ADDRESS_ID, address.getAddressId());
        params.put(EditAddressParam.ADDRESS_NAME, address.getAddressName());
        params.put(EditAddressParam.ADDRESS_STREET, address.getAddressStreet());
        params.put(EditAddressParam.POSTAL_CODE, address.getPostalCode());
        params.put(EditAddressParam.DISTRICT_ID, address.getDistrictId());
        params.put(EditAddressParam.CITY_ID, address.getCityId());
        params.put(EditAddressParam.PROVINCE_ID, address.getProvinceId());
        params.put(EditAddressParam.LATITUDE, address.getLatitude());
        params.put(EditAddressParam.LONGITUDE, address.getLongitude());
        params.put(EditAddressParam.RECEIVER_NAME, address.getReceiverName());
        params.put(EditAddressParam.RECEIVER_PHONE, address.getReceiverPhone());

        RequestParams requestParams = RequestParams.create();
        requestParams.putAllString(params);
        return requestParams;
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
                if (!isEditOperation) mView.stopPerformaceMonitoring();
                mView.finishActivity();
            }

            @Override
            public void onTimeout() {
                mView.finishLoading();
                mView.errorSaveAddress();
                mView.showErrorSnackbar("");
                if (!isEditOperation) mView.stopPerformaceMonitoring();
            }

            @Override
            public void onError(String error) {
                mView.finishLoading();
                mView.errorSaveAddress();
                mView.showErrorSnackbar(error);
                if (!isEditOperation) mView.stopPerformaceMonitoring();
            }

            @Override
            public void onNullData() {
                mView.finishLoading();
                mView.errorSaveAddress();
                mView.showErrorSnackbar("");
                if (!isEditOperation) mView.stopPerformaceMonitoring();
            }

            @Override
            public void onNoNetworkConnection() {
                mView.finishLoading();
                mView.errorSaveAddress();
                mView.showErrorSnackbar("");
                if (!isEditOperation) mView.stopPerformaceMonitoring();
            }
        };
    }

    private TKPDMapParam<String, String> getParam() {
        TKPDMapParam<String, String> params;
        Destination address = mView.getAddress();

        if (mView.isEdit()) {
            params = getParamEditAddress(address);
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

    private TKPDMapParam<String, String> getParamEditAddress(Destination address) {
        TKPDMapParam<String, String> param = getParamAddAddress(address);
        param.put(PARAM_ADDRESS_ID, (address.getAddressId() != null) ? address.getAddressId() : "");
        return param;
    }

}

