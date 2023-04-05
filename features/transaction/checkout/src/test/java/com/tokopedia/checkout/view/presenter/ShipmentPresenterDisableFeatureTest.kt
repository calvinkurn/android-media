package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.UnitTestFileUtils
import com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipmentAddressFormDataResponse
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import io.mockk.coEvery
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ShipmentPresenterDisableFeatureTest : BaseShipmentPresenterTest() {

    companion object {
        const val PATH_JSON_SAF_DISABLE_DROPSHIPPER = "assets/saf_disable_dropshipper.json"
        const val PATH_JSON_SAF_DISABLE_ORDER_PRIORITAS = "assets/saf_disable_order_prioritas.json"
        const val PATH_JSON_SAF_DISABLE_EGOLD = "assets/saf_disable_egold.json"
        const val PATH_JSON_SAF_DISABLE_PPP = "assets/saf_disable_ppp.json"
        const val PATH_JSON_SAF_DISABLE_DONATION = "assets/saf_disable_donation.json"
        const val PATH_JSON_SAF_DISABLE_ALL = "assets/saf_disable_all.json"
    }

    private val unitTestFileUtils = UnitTestFileUtils()
    private val shipmentMapper = ShipmentMapper()

    @Test
    fun disable_dropshipper() {
        // Given
        val dataResponse = gson.fromJson(
            unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_DROPSHIPPER),
            ShipmentAddressFormDataResponse::class.java
        )
        val data = shipmentMapper.convertToShipmentAddressFormData(dataResponse)

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns data

        // When
        presenter.processInitialLoadCheckoutPage(
            false,
            false,
            false
//            false,
//            false,
//            null,
//            "",
//            "",
//            false
        )

        // Then
        presenter.shipmentCartItemModelList.each { if (this is ShipmentCartItemModel) assertEquals(true, isDropshipperDisable) }
        presenter.shipmentCartItemModelList.each { if (this is ShipmentCartItemModel) assertEquals(false, isOrderPrioritasDisable) }
        assertNotNull(presenter.egoldAttributeModel)
        presenter.shipmentCartItemModelList.each {
            if (this is ShipmentCartItemModel) {
                cartItemModels.each {
                    assertEquals(
                        true,
                        isProtectionAvailable
                    )
                }
            }
        }
        assertNotNull(presenter.shipmentDonationModel)
    }

    @Test
    fun disable_order_prioritas() {
        // Given
        val dataResponse = gson.fromJson(
            unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_ORDER_PRIORITAS),
            ShipmentAddressFormDataResponse::class.java
        )
        val data = shipmentMapper.convertToShipmentAddressFormData(dataResponse)

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns data

        // When
        presenter.processInitialLoadCheckoutPage(
            false,
            false,
            false
//            false,
//            false,
//            null,
//            "",
//            "",
//            false
        )

        // Then
        presenter.shipmentCartItemModelList.each { if (this is ShipmentCartItemModel) assertEquals(false, isDropshipperDisable) }
        presenter.shipmentCartItemModelList.each { if (this is ShipmentCartItemModel) assertEquals(true, isOrderPrioritasDisable) }
        assertNotNull(presenter.egoldAttributeModel)
        presenter.shipmentCartItemModelList.each {
            if (this is ShipmentCartItemModel) {
                cartItemModels.each {
                    assertEquals(
                        true,
                        isProtectionAvailable
                    )
                }
            }
        }
        assertNotNull(presenter.shipmentDonationModel)
    }

    @Test
    fun disable_egold() {
        // Given
        val dataResponse = gson.fromJson(
            unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_EGOLD),
            ShipmentAddressFormDataResponse::class.java
        )
        val data = shipmentMapper.convertToShipmentAddressFormData(dataResponse)

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns data

        // When
        presenter.processInitialLoadCheckoutPage(
            false,
            false,
            false
//            false,
//            false,
//            null,
//            "",
//            "",
//            false
        )

        // Then
        presenter.shipmentCartItemModelList.each { if (this is ShipmentCartItemModel) assertEquals(false, isDropshipperDisable) }
        presenter.shipmentCartItemModelList.each { if (this is ShipmentCartItemModel) assertEquals(false, isOrderPrioritasDisable) }
        assertNull(presenter.egoldAttributeModel)
        presenter.shipmentCartItemModelList.each {
            if (this is ShipmentCartItemModel) {
                cartItemModels.each {
                    assertEquals(
                        true,
                        isProtectionAvailable
                    )
                }
            }
        }
        assertNotNull(presenter.shipmentDonationModel)
    }

    @Test
    fun disable_ppp() {
        // Given
        val dataResponse = gson.fromJson(
            unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_PPP),
            ShipmentAddressFormDataResponse::class.java
        )
        val data = shipmentMapper.convertToShipmentAddressFormData(dataResponse)

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns data

        // When
        presenter.processInitialLoadCheckoutPage(
            false,
            false,
            false
//            false,
//            false,
//            null,
//            "",
//            "",
//            false
        )

        // Then
        presenter.shipmentCartItemModelList.each { if (this is ShipmentCartItemModel) assertEquals(false, isDropshipperDisable) }
        presenter.shipmentCartItemModelList.each { if (this is ShipmentCartItemModel) assertEquals(false, isOrderPrioritasDisable) }
        assertNotNull(presenter.egoldAttributeModel)
        presenter.shipmentCartItemModelList.each {
            if (this is ShipmentCartItemModel) {
                cartItemModels.each {
                    assertEquals(
                        false,
                        isProtectionAvailable
                    )
                }
            }
        }
        assertNotNull(presenter.shipmentDonationModel)
    }

    @Test
    fun disable_donation() {
        // Given
        val dataResponse = gson.fromJson(
            unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_DONATION),
            ShipmentAddressFormDataResponse::class.java
        )
        val data = shipmentMapper.convertToShipmentAddressFormData(dataResponse)

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns data

        // When
        presenter.processInitialLoadCheckoutPage(
            false,
            false,
            false
//            false,
//            false,
//            null,
//            "",
//            "",
//            false
        )

        // Then
        presenter.shipmentCartItemModelList.each { if (this is ShipmentCartItemModel) assertEquals(false, isDropshipperDisable) }
        presenter.shipmentCartItemModelList.each { if (this is ShipmentCartItemModel) assertEquals(false, isOrderPrioritasDisable) }
        assertNotNull(presenter.egoldAttributeModel)
        presenter.shipmentCartItemModelList.each {
            if (this is ShipmentCartItemModel) {
                cartItemModels.each {
                    assertEquals(
                        true,
                        isProtectionAvailable
                    )
                }
            }
        }
        assertNull(presenter.shipmentDonationModel)
    }

    @Test
    fun disable_all() {
        // Given
        val dataResponse = gson.fromJson(
            unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_ALL),
            ShipmentAddressFormDataResponse::class.java
        )
        val data = shipmentMapper.convertToShipmentAddressFormData(dataResponse)

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns data

        // When
        presenter.processInitialLoadCheckoutPage(
            false,
            false,
            false
//            false,
//            false,
//            null,
//            "",
//            "",
//            false
        )

        // Then
        presenter.shipmentCartItemModelList.each { if (this is ShipmentCartItemModel) assertEquals(true, isDropshipperDisable) }
        presenter.shipmentCartItemModelList.each { if (this is ShipmentCartItemModel)assertEquals(true, isOrderPrioritasDisable) }
        assertNull(presenter.egoldAttributeModel)
        presenter.shipmentCartItemModelList.each {
            if (this is ShipmentCartItemModel) {
                cartItemModels.each {
                    assertEquals(
                        false,
                        isProtectionAvailable
                    )
                }
            }
        }
        assertNull(presenter.shipmentDonationModel)
    }
}

fun <T : Any> List<T>.each(action: T.() -> Unit) {
    for (item in this) {
        item.action()
    }
}
