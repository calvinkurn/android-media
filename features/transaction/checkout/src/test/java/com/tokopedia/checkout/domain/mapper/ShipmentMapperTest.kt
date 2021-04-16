package com.tokopedia.checkout.domain.mapper

import com.google.gson.Gson
import com.tokopedia.checkout.UnitTestFileUtils
import com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipmentAddressFormDataResponse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Created by Fajar Ulin Nuha on 15/11/18.
 */
class ShipmentMapperTest {

    private var unitTestFileUtils: UnitTestFileUtils? = null
    private var gson: Gson? = null
    private var shipmentMapper: ShipmentMapper? = null

    @Before
    fun setUp() {
        unitTestFileUtils = UnitTestFileUtils()
        gson = Gson()
        shipmentMapper = ShipmentMapper()
    }

    @Test
    fun convertToShipmentAddressFormDataWithContractPurchaseProtectionNoException() {
        // given
        val response = gson?.fromJson(unitTestFileUtils?.getJsonFromAsset(PATH_CONTRACT_PURCHASE_PROTECTION),
                ShipmentAddressFormDataResponse::class.java) ?: ShipmentAddressFormDataResponse()

        // when
        val cartShipmentAddressFormData = shipmentMapper?.convertToShipmentAddressFormData(response)

        // verify
        assertNotNull(cartShipmentAddressFormData?.groupAddress?.get(0)?.groupShop?.get(0)?.products?.get(0)?.purchaseProtectionPlanData)
    }

    companion object {
        private const val PATH_CONTRACT_PURCHASE_PROTECTION = "assets/contract_purchase_protection.json"
    }
}