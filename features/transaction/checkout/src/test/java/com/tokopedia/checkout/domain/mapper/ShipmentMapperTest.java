package com.tokopedia.checkout.domain.mapper;

import com.google.gson.Gson;
import com.tokopedia.UnitTestFileUtils;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transactiondata.entity.response.shippingaddressform.ShipmentAddressFormDataResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Fajar Ulin Nuha on 15/11/18.
 */
public class ShipmentMapperTest {

    private static final String PATH_CONTRACT_PURCHASE_PROTECTION = "assets/contract_purchase_protection.json";
    private static final String PATH_CONTRACT_NULL_PURCHASE_PROTECTION = "assets/contract_purchase_protection_null.json";

    private UnitTestFileUtils unitTestFileUtils;
    private Gson gson;
    private ShipmentMapper shipmentMapper;

    @Before
    public void setUp() {
        MapperUtil mapperUtil = new MapperUtil();
        unitTestFileUtils = new UnitTestFileUtils();
        gson = new Gson();
        shipmentMapper = new ShipmentMapper(mapperUtil);
    }


    @Test
    public void convertToShipmentAddressFormDataWithContractPurchaseProtectionNoException() {
        // given
        ShipmentAddressFormDataResponse response =
                gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_CONTRACT_PURCHASE_PROTECTION),
                        ShipmentAddressFormDataResponse.class);

        // when
        CartShipmentAddressFormData result = shipmentMapper.convertToShipmentAddressFormData(response);

        // verify
        Assert.assertNotNull(result.getGroupAddress().get(0).getGroupShop()
                .get(0).getProducts().get(0).getPurchaseProtectionPlanData());
    }

    @Test
    public void convertToShipmentAddressFormDataWithContractNullPurchaseProtectionNoException() {
        // given
        ShipmentAddressFormDataResponse response =
                gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_CONTRACT_NULL_PURCHASE_PROTECTION),
                        ShipmentAddressFormDataResponse.class);

        // when
        CartShipmentAddressFormData result = shipmentMapper.convertToShipmentAddressFormData(response);

        // verify
        Assert.assertNull(result.getGroupAddress().get(0).getGroupShop()
                .get(0).getProducts().get(0).getPurchaseProtectionPlanData());
    }
}