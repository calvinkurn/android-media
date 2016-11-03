package com.tokopedia.tkpd.shipping.presenter;

import android.os.Bundle;

import com.tokopedia.tkpd.shipping.model.editshipping.EditShippingCouriers;
import com.tokopedia.tkpd.shipping.model.editshipping.ProvinceCitiesDistrict;
import com.tokopedia.tkpd.shipping.model.editshipping.ShopShipping;
import com.tokopedia.tkpd.shipping.model.openshopshipping.OpenShopData;

import java.util.List;
import java.util.Map;

/**
 * Created by Kris on 2/23/2016.
 TOKOPEDIA
 */
public interface EditShippingPresenter {

    String COURIER_ORIGIN = "courier_origin";

    String SHIPMENT_IDS = "shipment_ids";

    String POSTAL = "postal";

    String SHOP_POSTAL = "shop_postal";

    String SHOP_ID = "shop_id";

    String ADDR_STREET = "addr_street";

    String LATITUDE = "latitude";

    String LONGITUDE = "longitude";

    String OS_TYPE = "os_type";

    String USER_ID = "user_id";

    String SERVICE_ID = "service_id";

    String DISTRICT_ID = "shop_courier_origin";

    int MINIMUM_WEIGHT_POLICY_CHARACTER_SIZE = 0;

    void fetchData();

    void bindDataToView(EditShippingCouriers model);

    void fetchDataByLocation(String locationID);

    void fetchDataByLocationOpenShop(String locationID);

    void fetchDataOpenShop();

    void bindDataToViewOpenShop(OpenShopData model);

    void setServiceCondition(boolean isChecked, int serviceIndex, int courierIndex);

    void submitValue();

    void refreshData();

    boolean editShippingParamsValid();

    List<ProvinceCitiesDistrict> getProvinceCityDistrictList();

    ShopShipping getShopInformation();

    EditShippingCouriers getShopModel();

    void setShopModelFromSavedInstance(EditShippingCouriers model);

    OpenShopData getOpenShopModel();

    void saveOpenShopModel();

    void setOpenShopModelFromSavedInstance(OpenShopData model);

    void dataWebViewResource(int courierIndex, String webViewURL);

    void setCourierAdditionalOptionConfig(int courierIndex, String additionalOptionQueries);

    String getCourierAdditionalOptionsURL(int courierIndex);

    void onViewDestroyed();

    void setSavedInstance(Bundle savedInstance);

    OpenShopData passShippingData();

    void savePostalCode(String s);

    void saveAddressArea(String s);
}
