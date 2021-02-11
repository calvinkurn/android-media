package com.tokopedia.editshipping.presenter;

import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tokopedia.editshipping.R;
import com.tokopedia.editshipping.analytics.EditShippingAnalytics;
import com.tokopedia.editshipping.data.interactor.EditShippingInteractorImpl;
import com.tokopedia.editshipping.data.interactor.EditShippingRetrofitInteractor;
import com.tokopedia.editshipping.data.network.ShippingNetworkParam;
import com.tokopedia.editshipping.domain.ValidateShippingMapper;
import com.tokopedia.editshipping.domain.ValidateShippingUseCase;
import com.tokopedia.editshipping.domain.model.ValidateShippingModel;
import com.tokopedia.editshipping.domain.model.editshipping.Courier;
import com.tokopedia.editshipping.domain.model.editshipping.EditShippingCouriers;
import com.tokopedia.editshipping.domain.model.editshipping.ProvinceCitiesDistrict;
import com.tokopedia.editshipping.domain.model.editshipping.ShopShipping;
import com.tokopedia.editshipping.domain.model.openshopshipping.OpenShopData;
import com.tokopedia.editshipping.ui.EditShippingFragment;
import com.tokopedia.editshipping.ui.EditShippingViewListener;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.logisticCommon.data.entity.address.DistrictRecommendationAddress;
import com.tokopedia.logisticCommon.data.entity.address.Token;
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill;
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeUseCase;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by Kris on 2/23/2016.
 * Presenter for EditShipping
 */
public class EditShippingPresenterImpl implements EditShippingPresenter {

    private EditShippingViewListener view;

    private EditShippingRetrofitInteractor editShippingRetrofitInteractor;

    private RevGeocodeUseCase revGeocodeUseCase;

    private ShopShipping shopInformation;

    private List<Courier> courierList;

    private List<ProvinceCitiesDistrict> provinceCitiesDistrict;

    private List<String> activatedServices = new ArrayList<>();

    private Map<String, String> serviceCourierPair = new HashMap<>();

    private Set<String> activatedCourier = new HashSet<>();

    private String temporaryWebViewResource;

    private EditShippingCouriers model;

    private OpenShopData openShopModel;

    private DistrictRecommendationAddress selectedAddress;

    private UserSessionInterface userSession;

    private ValidateShippingUseCase validateShippingUseCase;

    private ValidateShippingModel validateBoData;

    private ValidateShippingMapper validateShippingMapper;

    public EditShippingPresenterImpl(EditShippingViewListener view) {
        this.view = view;
        editShippingRetrofitInteractor = new EditShippingInteractorImpl();
        revGeocodeUseCase = new RevGeocodeUseCase(view.getMainContext(), new GraphqlUseCase());
        userSession = new UserSession(view.getMainContext());
        validateShippingMapper = new ValidateShippingMapper();
        validateShippingUseCase = new ValidateShippingUseCase(view.getMainContext(), new GraphqlUseCase(), validateShippingMapper);
    }

    @Override
    public void fetchData() {
        editShippingRetrofitInteractor.initiateCourierList(view.getMainContext(),
                new EditShippingRetrofitInteractor.CourierListListener() {
                    @Override
                    public void onSuccess(EditShippingCouriers model) {
                        initiateDatas(model);
                        bindDataToView(model);
                        getReverseGeocode();
                    }

                    @Override
                    public void onFailed(String error) {
                        view.finishStartingFragment();
                        view.onFragmentTimeout();
                    }

                    @Override
                    public void onTimeout(String timeoutError) {
                        view.finishStartingFragment();
                        view.onFragmentTimeout();
                    }

                    @Override
                    public void onNoConnection() {
                        view.finishStartingFragment();
                        view.onFragmentNoConnection();
                    }
                });
    }

    private void getReverseGeocode() {
        if (shopInformation == null) return;
        String latlng = String.format("%s,%s",
                shopInformation.getShopLatitude(), shopInformation.getShopLongitude());
        revGeocodeUseCase
                .execute(latlng)
                .subscribe(new Subscriber<KeroMapsAutofill>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.setGeoAddress(latlng);
                    }

                    @Override
                    public void onNext(KeroMapsAutofill keroMapsAutofill) {
                        if (!keroMapsAutofill.getData().getFormattedAddress().isEmpty()) {
                            view.setGeoAddress(keroMapsAutofill.getData().getFormattedAddress());
                        } else {
                            view.setGeoAddress(latlng);
                        }
                    }
                });
    }

    @Override
    public void bindDataToView(EditShippingCouriers model) {
        this.model = model;
        if (selectedAddress == null) {
            if (shopInformation.provinceName == null || shopInformation.cityName == null ||
                    shopInformation.districtName == null) {
                view.setLocationProvinceCityDistrict();
            } else {
                view.setLocationProvinceCityDistrict(shopInformation.provinceName
                        , shopInformation.cityName
                        , shopInformation.districtName);
            }
        } else {
            shopInformation.provinceName = selectedAddress.getProvinceName();
            shopInformation.provinceId = selectedAddress.getProvinceId();
            shopInformation.cityName = selectedAddress.getCityName();
            shopInformation.cityId = selectedAddress.getCityId();
            shopInformation.districtName = selectedAddress.getDistrictName();
            shopInformation.districtId = selectedAddress.getDistrictId();
        }
        view.onShowViewAfterLoading();
        displayCourierList(model);
        setFragmentHeaderData();
        view.finishStartingFragment();
    }

    private void setFragmentHeaderData() {
        view.setShopDetailedInformation(shopInformation);
        view.setShopLocationData(shopInformation);
    }

    @Override
    public void fetchDataByLocation(String locationID) {
        editShippingRetrofitInteractor.getCourierList(view.getMainContext(),
                ShippingNetworkParam.paramShopShipping(locationID),
                fetchDataByLocationListener(locationID));
    }

    @Override
    public void fetchDataByLocationOpenShop(final String locationID) {
        editShippingRetrofitInteractor.getOpenShopData(view.getMainContext(),
                ShippingNetworkParam.paramShopShipping(locationID),
                new EditShippingRetrofitInteractor.getOpenShopDataListener() {
                    @Override
                    public void onSuccess(OpenShopData model) {
                        openShopModel.setShipment(model.getShipment());
                        setCourierModel(model.getShipment());
                        shopInformation.setShopDistrictId(Integer.parseInt(locationID));
                        if (selectedAddress == null) {
                            view.refreshLocationViewListener(shopInformation);
                        } else {
                            view.refreshLocationViewListener(selectedAddress);
                        }
                        sortCourierOpenShop(model);
                        activateCourierServices(model.getShipment());
                        displayCourierOpenShop(model);
                        view.onShowViewAfterLoading();
                        view.finishLoading();
                    }

                    @Override
                    public void onFailed() {
                        view.locationDialogTimeoutListener();
                    }

                    @Override
                    public void onTimeout() {
                        view.locationDialogTimeoutListener();
                    }

                    @Override
                    public void onNoConnection() {
                        view.showErrorToast(view.getMainContext().getString(com.tokopedia.abstraction.R.string.msg_no_connection));
                    }
                });
    }

    @Override
    public void fetchDataOpenShop() {
        editShippingRetrofitInteractor.getOpenShopData(view.getMainContext(),
                new HashMap<String, String>(),
                new EditShippingRetrofitInteractor.getOpenShopDataListener() {
                    @Override
                    public void onSuccess(OpenShopData model) {
                        initiateDatasOpenShop(model);
                        bindDataToViewOpenShop(model);
                        getReverseGeocode();
                    }

                    @Override
                    public void onFailed() {

                    }

                    @Override
                    public void onTimeout() {
                        view.finishStartingFragment();
                        view.onFragmentTimeout();
                    }

                    @Override
                    public void onNoConnection() {
                        view.finishStartingFragment();
                        view.onFragmentNoConnection();
                    }
                });
    }

    @Override
    public void bindDataToViewOpenShop(OpenShopData model) {
        openShopModel = model;
        if (selectedAddress != null) {
            view.setLocationProvinceCityDistrict(selectedAddress.getProvinceName()
                    , selectedAddress.getCityName()
                    , selectedAddress.getDistrictName());
        } else if (openShopModel.getShopShipping().provinceName == null || openShopModel.getShopShipping().provinceName.isEmpty()) {
            view.setLocationProvinceCityDistrict();
            setShopInfoFromOpenShopData(model);
        } else {
            view.setLocationProvinceCityDistrict(openShopModel.getShopShipping().provinceName
                    , openShopModel.getShopShipping().cityName
                    , openShopModel.getShopShipping().districtName);
        }
        view.onShowViewAfterLoading();
        displayCourierOpenShop(model);
        setFragmentHeaderData();
        view.finishStartingFragment();
    }

    private void setShopInfoFromOpenShopData(OpenShopData model) {
        if (model.getOpenShopHashMap() != null) {
            String addressStreet = model.getOpenShopHashMap().get(EditShippingPresenter.ADDR_STREET);
            if (addressStreet != null) {
                shopInformation.addrStreet = addressStreet;
            }

            String latitude = model.getOpenShopHashMap().get(EditShippingPresenter.LATITUDE);
            if (latitude != null) {
                shopInformation.latitude = latitude;
            }

            String longitude = model.getOpenShopHashMap().get(EditShippingPresenter.LONGITUDE);
            if (longitude != null) {
                shopInformation.longitude = longitude;
            }

            String selectedAddressStr = model.getOpenShopHashMap().get(EditShippingPresenter.SELECTED_ADDRESS);
            if (selectedAddressStr != null && selectedAddressStr.length() > 0) {
                selectedAddress = new Gson().fromJson(selectedAddressStr, DistrictRecommendationAddress.class);

                view.setLocationProvinceCityDistrict(selectedAddress.getProvinceName(),
                        selectedAddress.getCityName(), selectedAddress.getDistrictName());

                view.initializeZipCodes();
            }

            String postalCode = model.getOpenShopHashMap().get(EditShippingPresenter.SHOP_POSTAL);
            if (postalCode != null) {
                shopInformation.postalCode = postalCode;
            }
        }
    }

    private EditShippingRetrofitInteractor.CourierListListener fetchDataByLocationListener(final String districtID) {
        return new EditShippingRetrofitInteractor.CourierListListener() {
            @Override
            public void onSuccess(EditShippingCouriers model) {
                EditShippingPresenterImpl.this.model.setCourier(model.getCourier());
                setCourierModel(model.courier);
                shopInformation.setShopDistrictId(Integer.parseInt(districtID));
                if (selectedAddress == null) {
                    view.refreshLocationViewListener(shopInformation);
                } else {
                    view.refreshLocationViewListener(selectedAddress);
                }
                sortCourier(model);
                activateCourierServices(model.courier);
                displayCourierList(model);
                view.onShowViewAfterLoading();
                view.finishLoading();
            }

            @Override
            public void onFailed(String error) {

            }

            @Override
            public void onTimeout(String timeoutError) {
                view.locationDialogTimeoutListener();
            }

            @Override
            public void onNoConnection() {
                view.showErrorToast(view.getMainContext().getString(com.tokopedia.abstraction.R.string.msg_no_connection));
            }
        };
    }

    private void displayCourierList(EditShippingCouriers editShippingCouriers) {
        for (int i = 0; i < editShippingCouriers.courier.size(); i++) {
            Courier currentCourier = editShippingCouriers.courier.get(i);
            view.addCourier(currentCourier, i);
            setCourierService(currentCourier);
            populateCurrentAdditionalOptions(currentCourier, currentCourier.urlAdditionalOption);
        }
    }

    private void displayCourierOpenShop(OpenShopData data) {
        for (int i = 0; i < data.courier.size(); i++) {
            Courier currentCourier = data.courier.get(i);
            view.addCourier(currentCourier, i);
            setCourierService(currentCourier);
            populateCurrentAdditionalOptions(currentCourier, currentCourier.urlAdditionalOption);
        }
    }

    private void setCourierService(Courier currentCourier) {
        if (currentCourier.available.equals("1")) {
            setCourierServiceBehavior(currentCourier);
        }
    }

    private void populateCurrentAdditionalOptions(Courier currentCourier, String urlAdditionalOptions) {
        if (!urlAdditionalOptions.isEmpty() && !urlAdditionalOptions.equals("0")) {
            try {
                currentCourier.setAdditionalOptionDatas(splitQuery(URI.create(urlAdditionalOptions)));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void setCourierModel(List<Courier> courierList) {
        this.courierList = courierList;
    }

    private void activateCourierServices(List<Courier> courierList) {
        for (int i = 0; i < courierList.size(); i++) {
            if (courierList.get(i).available.equals("1")) {
                for (int j = 0; j < courierList.get(i).services.size(); j++) {
                    courierList.get(i).services.get(j).setActive("1");
                }
            }
        }
    }

    @Override
    public void setServiceCondition(boolean isChecked, int serviceIndex, int courierIndex) {
        if (isChecked) {
            courierList.get(courierIndex).services.get(serviceIndex).setActive("1");
            activatedServices.add(courierList.get(courierIndex).services.get(serviceIndex).id);
        } else {
            courierList.get(courierIndex).services.get(serviceIndex).setActive("0");
            activatedServices.remove(courierList.get(courierIndex).services.get(serviceIndex).id);
        }

    }

    @Override
    public void submitValue() {
        validateParameters();
    }

    private void sendData() {
        scanActivatedCourier();
        Map<String, String> shippingUpdateParams = new HashMap<>();
        putDataToHashMap(shippingUpdateParams);
        editShippingRetrofitInteractor.updateCourierChanges(view.getMainContext()
                , shippingUpdateParams
                , new EditShippingRetrofitInteractor.ShippingUpdateListener() {
                    @Override
                    public void onSuccess(String statusMessage) {
                        view.finishLoading();
                        view.refreshData(statusMessage);
                    }

                    @Override
                    public void onFailed(String messageError) {
                        activatedCourier.clear();
                        view.showErrorToast(messageError);
                    }

                    @Override
                    public void onTimeout() {
                        view.showErrorToast(view.getMainContext().getString(com.tokopedia.abstraction.R.string.title_try_again));
                    }

                    @Override
                    public void onNoConnection() {
                        view.showErrorToast(view.getMainContext().getString(com.tokopedia.abstraction.R.string.msg_no_connection));
                    }
                });
        Timber.d("PORING %s", compiledShippingId());
    }

    private void putDataToHashMap(Map<String, String> shippingParams) {
        shippingParams.put(SHIPMENT_IDS, compiledShippingId());
        shippingParams.put(POSTAL, view.getZipCode());
        shippingParams.put(ADDR_STREET, view.getStreetAddress());
        shippingParams.put(SHOP_ID, userSession.getShopId());
        shippingParams.put(LONGITUDE, shopInformation.longitude);
        shippingParams.put(LATITUDE, shopInformation.latitude);
        if (selectedAddress != null) {
            shippingParams.put(SELECTED_ADDRESS, new Gson().toJson(selectedAddress));
            shippingParams.put(DISTRICT_ID, String.valueOf(selectedAddress.getDistrictId()));
            shippingParams.put(COURIER_ORIGIN, String.valueOf(selectedAddress.getDistrictId()));
        } else {
            shippingParams.put(COURIER_ORIGIN, shopInformation.districtId.toString());
            shippingParams.put(DISTRICT_ID, shopInformation.districtId.toString());
        }
        addAdditionalOptionsConfigurations(shippingParams);
    }

    private void putDataToHashMapOpenShop(Map<String, String> shippingParams) {
        shippingParams.put(SHIPMENT_IDS, compiledShippingId());
        shippingParams.put(SHOP_POSTAL, view.getZipCode());
        shippingParams.put(ADDR_STREET, view.getStreetAddress());
        shippingParams.put(SHOP_ID, userSession.getShopId());
        shippingParams.put(LONGITUDE, shopInformation.longitude);
        shippingParams.put(LATITUDE, shopInformation.latitude);
        if (selectedAddress != null) {
            shippingParams.put(SELECTED_ADDRESS, new Gson().toJson(selectedAddress));
            shippingParams.put(COURIER_ORIGIN, String.valueOf(selectedAddress.getDistrictId()));
            shippingParams.put(DISTRICT_ID, String.valueOf(selectedAddress.getDistrictId()));
        } else {
            shippingParams.put(COURIER_ORIGIN, shopInformation.districtId.toString());
            shippingParams.put(DISTRICT_ID, shopInformation.districtId.toString());
        }
        addAdditionalOptionsConfigurations(shippingParams);
    }

    private void scanActivatedCourier() {
        for (int i = 0; i < activatedServices.size(); i++) {
            activatedCourier.add(serviceCourierPair.get(activatedServices.get(i)));
        }
    }

    @Override
    public void refreshData() {
        activatedServices.clear();
        serviceCourierPair.clear();
        activatedCourier.clear();
    }

    @Override
    public boolean editShippingParamsValid() {
        if (view.getZipCode().isEmpty()) {
            EditShippingAnalytics.eventCreateShopFillLogisticError();
            view.zipCodeEmpty();
            return false;
        } else if (activatedServices.isEmpty()) {
            EditShippingAnalytics.eventCreateShopFillLogisticError();
            view.noServiceChosen();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<ProvinceCitiesDistrict> getProvinceCityDistrictList() {
        return provinceCitiesDistrict;
    }

    @Override
    public ShopShipping getShopInformation() {
        return shopInformation;
    }

    private void setShopInformation(ShopShipping shopInformation) {
        this.shopInformation = shopInformation;
    }

    @Override
    public EditShippingCouriers getShopModel() {
        return this.model;
    }

    @Override
    public void setShopModelFromSavedInstance(EditShippingCouriers model) {
        this.model = model;
        initiateDatas(model);
    }

    private void initiateDatas(EditShippingCouriers model) {
        if (model != null) {
            setShopInformation(model.shopShipping);
            setCourierModel(model.courier);
            setLocationList(model.getProvincesCitiesDistricts());
            sortCourier(model);
        }
    }

    private void initiateDatasOpenShop(OpenShopData model) {
        if (model != null) {
            if (model.getShopShipping() != null && model.getShopShipping().provinceName != null
                    && !model.getShopShipping().provinceName.isEmpty()) {
                shopInformation = model.getShopShipping();
            }
            if (EditShippingPresenterImpl.this.shopInformation == null) {
                EditShippingPresenterImpl.this.shopInformation = new ShopShipping();
                model.setShopShipping(shopInformation);
            } else {
                setShopInformation(model.getShopShipping());
            }
            setCourierModel(model.getShipment());
            setLocationList(model.getProvincesCitiesDistricts());
        }

    }

    @Override
    public OpenShopData getOpenShopModel() {
        return openShopModel;
    }

    @Override
    public void saveOpenShopModel() {
        if (openShopModel != null && shopInformation != null) {
            openShopModel.setShopShipping(shopInformation);
        }
    }

    @Override
    public void setOpenShopModelFromSavedInstance(OpenShopData model) {
        this.openShopModel = model;
        initiateDatasOpenShop(model);
    }

    @Override
    public void dataWebViewResource(final int courierIndex, String webViewUrl) {
        HashMap<String, String> params = new HashMap<>();
        params.put(USER_ID, userSession.getUserId());
        params.put(OS_TYPE, "1");
        params.put(SERVICE_ID, compiledSelectedServicesString(courierIndex));
        editShippingRetrofitInteractor.getShippingDetailWebView(view.getMainContext(),
                webViewUrl,
                params,
                new EditShippingRetrofitInteractor.getShippingDetailListener() {
                    @Override
                    public void onSuccess(String webResources) {
                        temporaryWebViewResource = webResources;
                        view.openWebView(webResources, courierIndex);
                    }

                    @Override
                    public void onFailed() {

                    }

                    @Override
                    public void onTimeout() {
                        view.showErrorToast(view
                                .getMainContext().getString(R.string.title_verification_timeout));
                    }

                    @Override
                    public void onNoConnection() {
                        view.showErrorToast(view
                                .getMainContext().getString(com.tokopedia.abstraction.R.string.msg_no_connection));
                    }
                });
    }

    @Override
    public void setCourierAdditionalOptionConfig(Integer courierIndex, String additionalOptionQueries) {
        if (courierIndex != null) {
            courierList.get(courierIndex).getAdditionalOptionDatas().clear();
            try {
                URI uri = URI.create(additionalOptionQueries);
                courierList.get(courierIndex).setAdditionalOptionDatas(splitQuery(uri));
                courierList.get(courierIndex).urlAdditionalOption = additionalOptionQueries;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                view.openWebView(temporaryWebViewResource, courierIndex);
                Toast.makeText(view.getMainContext(),
                        view.getMainContext().getString(R.string.error_shipping_dialog_webview),
                        Toast.LENGTH_LONG).show();
            } catch (IllegalArgumentException e) {
                view.openWebView(temporaryWebViewResource, courierIndex);
                Toast.makeText(view.getMainContext(),
                        view.getMainContext().getString(R.string.error_shipping_dialog_webview),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public String getCourierAdditionalOptionsURL(int courierIndex) {
        return courierList.get(courierIndex).urlAdditionalOption;
    }

    @Override
    public void onViewDestroyed() {
        editShippingRetrofitInteractor.onViewDestroyed();
    }

    @Override
    public void setSavedInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null
                && savedInstanceState.containsKey(EditShippingFragment.CURRENT_COURIER_MODEL)) {
            setShopModelFromSavedInstance((EditShippingCouriers) savedInstanceState
                    .getParcelable(EditShippingFragment.CURRENT_COURIER_MODEL));
        } else if (savedInstanceState != null
                && savedInstanceState.containsKey(EditShippingFragment.CURRENT_OPEN_SHOP_MODEL)) {
            setOpenShopModelFromSavedInstance((OpenShopData) savedInstanceState
                    .getParcelable(EditShippingFragment.CURRENT_OPEN_SHOP_MODEL));
        } else if (savedInstanceState != null
                && savedInstanceState.containsKey(EditShippingFragment.RESUME_OPEN_SHOP_DATA_KEY)) {
            setOpenShopModelFromSavedInstance((OpenShopData) savedInstanceState
                    .getParcelable(EditShippingFragment.RESUME_OPEN_SHOP_DATA_KEY));
        }
    }

    @Override
    public OpenShopData passShippingData() {
        scanActivatedCourier();
        HashMap<String, String> shippingUpdateParams = new HashMap<>();
        putDataToHashMapOpenShop(shippingUpdateParams);
        openShopModel.setOpenShopHashMap(shippingUpdateParams);
//        openShopModel.getShopShipping().postalCode = view.getZipCode();
//        openShopModel.getShopShipping().addrStreet = view.getStreetAddress();
        return openShopModel;
    }

    @Override
    public void savePostalCode(String s) {
        if (openShopModel != null) openShopModel.getShopShipping().postalCode = s;
        else if (model != null) model.shopShipping.postalCode = s;
    }

    @Override
    public void saveAddressArea(String s) {
        if (openShopModel != null) openShopModel.getShopShipping().addrStreet = s;
        else if (model != null) model.shopShipping.addrStreet = s;
    }

    @Override
    public void setSelectedAddress(DistrictRecommendationAddress address) {
        selectedAddress = address;
    }

    @Override
    public DistrictRecommendationAddress getselectedAddress() {
        return selectedAddress;
    }

    @Override
    public Token getToken() {
        if (model != null) {
            return model.getToken();
        } else if (openShopModel != null) {
            return openShopModel.getToken();
        }
        return null;
    }

    private void sortCourier(EditShippingCouriers editShippingCouriers) {
        Collections.sort(editShippingCouriers.courier, new Comparator<Courier>() {
            @Override
            public int compare(Courier lhs, Courier rhs) {
                return ((Integer) rhs.getCourierWeight()).compareTo(lhs.getCourierWeight());
            }
        });
    }

    private void sortCourierOpenShop(OpenShopData data) {
        Collections.sort(data.courier, new Comparator<Courier>() {
            @Override
            public int compare(Courier lhs, Courier rhs) {
                return ((Integer) rhs.getCourierWeight()).compareTo(lhs.getCourierWeight());
            }
        });
    }

    private void setCourierServiceBehavior(Courier currentCourier) {
        for (int j = 0; j < currentCourier.services.size(); j++) {
            serviceCourierPair.put(currentCourier.services.get(j).id, currentCourier.id);
            sortActivatedService(serviceActivated(currentCourier.services.get(j).active),
                    currentCourier.services.get(j).id);
        }
    }

    private boolean serviceActivated(String activatedState) {
        return activatedState.equals("1");
    }

    private void sortActivatedService(boolean isChecked, String serviceID) {
        if (isChecked) activatedServices.add(serviceID);
    }

    private void validateParameters() {
        if (view.getZipCode().isEmpty()) view.zipCodeEmpty();
        else if (activatedServices.isEmpty()) view.noServiceChosen();
        else {
            view.showLoading();
            sendData();
        }
    }

    private void setLocationList(List<ProvinceCitiesDistrict> listProvinceCitiesDistricts) {
        provinceCitiesDistrict = listProvinceCitiesDistricts;
    }

    private String compiledShippingId() {
        JSONObject jsonCompiled = new JSONObject();
        JSONObject shippingPackage;
        try {
            for (int i = 0; i < courierList.size(); i++) {
                if (activatedCourier.contains(courierList.get(i).id)
                        && courierList.get(i).available.equals("1")) {
                    shippingPackage = new JSONObject();
                    for (int j = 0; j < courierList.get(i).services.size(); j++) {
                        if (activatedServices.contains(courierList.get(i).services.get(j).id)) {
                            shippingPackage.put(courierList.get(i).services.get(j).id, "1");
                        }
                        jsonCompiled.put(courierList.get(i).id, shippingPackage);
                    }
                }
            }
            return jsonCompiled.toString();
        } catch (JSONException e) {
            return "";
        }
    }

    private String compiledSelectedServicesString(int courierIndex) {
        String selectedServices = "";
        for (int i = 0; i < courierList.get(courierIndex).services.size(); i++) {
            if (courierList.get(courierIndex).services.get(i).getActive()) {
                selectedServices = selectedServices
                        + courierList.get(courierIndex).services.get(i).id
                        + ",";
            }
        }
        if (!selectedServices.equals("")) {
            selectedServices = selectedServices.substring(0, selectedServices.length() - 1);
            return selectedServices;
        } else return "0";
    }

    private Map<String, String> splitQuery(URI uri) throws UnsupportedEncodingException {
        Map<String, String> queryPairs = new LinkedHashMap<>();
        String query = uri.getQuery();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                queryPairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            }
        }
        return queryPairs;
    }

    private void addAdditionalOptionsConfigurations(Map<String, String> shippingUpdateParams) {
        for (int i = 0; i < courierList.size(); i++) {
            shippingUpdateParams.putAll(courierList.get(i).getAdditionalOptionDatas());
        }
    }

    @Override
    public void validateBo(int shopId, String compiledShippingId) {
        Map<String, Object> param = new HashMap<>();
        param.put(ValidateShippingUseCase.SHOP_ID, shopId);
        param.put(ValidateShippingUseCase.SHIPMENT_IDS, compiledShippingId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(param);
        validateShippingUseCase.execute(requestParams, new Subscriber<ValidateShippingModel>() {
            @Override
            public void onCompleted() {
                //no-op
            }

            @Override
            public void onError(Throwable e) {
                 view.showErrorToast(ErrorHandler.getErrorMessage(view.getMainContext(), e));
            }

            @Override
            public void onNext(ValidateShippingModel validateShippingModel) {
                view.validateShowPopup(validateShippingModel);
            }
        });
    }

    @Override
    public int getShopId() {
        int shopId = Integer.parseInt(userSession.getShopId());
        return shopId;
    }

    @NotNull
    @Override
    public String getCompiledShippingId() {
        scanActivatedCourier();
        return compiledShippingId();
    }
}
