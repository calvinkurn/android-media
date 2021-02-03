package com.tokopedia.checkout.domain.mapper

import com.google.gson.Gson
import com.tokopedia.checkout.UnitTestFileUtils
import com.tokopedia.checkout.data.model.response.shipment_address_form.ShipmentAddressFormDataResponse
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
        val response = gson!!.fromJson(unitTestFileUtils!!.getJsonFromAsset(PATH_CONTRACT_PURCHASE_PROTECTION),
                ShipmentAddressFormDataResponse::class.java)

        // when
        val (_, _, _, _, _, _, _, groupAddress) = shipmentMapper!!.convertToShipmentAddressFormData(response)

        // verify
        assertNotNull(groupAddress!![0].groupShop[0].products[0].purchaseProtectionPlanData)
    }

    @Test
    fun convertToShipmentAddressFormDataWithContractNullPurchaseProtectionNoException() {
        // given
        val response = gson!!.fromJson(unitTestFileUtils!!.getJsonFromAsset(PATH_CONTRACT_NULL_PURCHASE_PROTECTION),
                ShipmentAddressFormDataResponse::class.java)

        // when
        val (_, _, _, _, _, _, _, groupAddress) = shipmentMapper!!.convertToShipmentAddressFormData(response)

        // verify
        assertNull(groupAddress!![0].groupShop[0].products[0].purchaseProtectionPlanData)
    }

    companion object {
        private const val PATH_CONTRACT_PURCHASE_PROTECTION = "assets/contract_purchase_protection.json"
        private const val PATH_CONTRACT_NULL_PURCHASE_PROTECTION = "assets/contract_purchase_protection_null.json"
    }
}