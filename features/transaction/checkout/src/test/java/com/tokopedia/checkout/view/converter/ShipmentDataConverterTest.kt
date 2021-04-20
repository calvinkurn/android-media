package com.tokopedia.checkout.view.converter

import com.tokopedia.checkout.domain.model.cartshipmentform.*
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ShipmentDataConverterTest {

    private val shipmentDataConverter = ShipmentDataConverter()

    // Bebas Ongkir Extra
    @Test
    fun `WHEN shop is fulfillment THEN should use fulfillment name as shop location`() {
        // Given
        val expectedFulfillmentName = "tokocabang"
        val cartShipmentAddressFormData = CartShipmentAddressFormData(groupAddress = listOf(
                GroupAddress().apply {
                    groupShop = listOf(
                            GroupShop().apply {
                                isFulfillment = true
                                fulfillmentName = expectedFulfillmentName
                                shipmentInformationData = ShipmentInformationData(shopLocation = "shop location")
                                shop = Shop()
                            }
                    )
                    userAddress = UserAddress()
                }
        ))

        // When
        val result = shipmentDataConverter.getShipmentItems(cartShipmentAddressFormData, false)

        // Then
        assertEquals(expectedFulfillmentName, result.first().shopLocation)
    }

    @Test
    fun `WHEN shop is not fulfillment THEN should use city name as shop location`() {
        // Given
        val expectedShopLocation = "shop location"
        val cartShipmentAddressFormData = CartShipmentAddressFormData(groupAddress = listOf(
                GroupAddress().apply {
                    groupShop = listOf(
                            GroupShop().apply {
                                isFulfillment = false
                                fulfillmentName = "tokocabang"
                                shipmentInformationData = ShipmentInformationData(shopLocation = expectedShopLocation)
                                shop = Shop()
                            }
                    )
                    userAddress = UserAddress()
                }
        ))

        // When
        val result = shipmentDataConverter.getShipmentItems(cartShipmentAddressFormData, false)

        // Then
        assertEquals(expectedShopLocation, result.first().shopLocation)
    }

    @Test
    fun `WHEN shop have Bebas Ongkir Extra THEN should use bebas ongkir extra url as free shipping badge url`() {
        // Given
        val expectedBadgeUrl = "bebas ongkir extra url"
        val cartShipmentAddressFormData = CartShipmentAddressFormData(groupAddress = listOf(
                GroupAddress().apply {
                    groupShop = listOf(
                            GroupShop().apply {
                                shipmentInformationData = ShipmentInformationData(freeShippingExtra = FreeShippingData(true, expectedBadgeUrl))
                                shop = Shop()
                            }
                    )
                    userAddress = UserAddress()
                }
        ))

        // When
        val result = shipmentDataConverter.getShipmentItems(cartShipmentAddressFormData, false)

        // Then
        assertEquals(expectedBadgeUrl, result.first().freeShippingBadgeUrl)
    }

    @Test
    fun `WHEN shop have Bebas Ongkir THEN should use bebas ongkir url as free shipping badge url`() {
        // Given
        val expectedBadgeUrl = "bebas ongkir url"
        val cartShipmentAddressFormData = CartShipmentAddressFormData(groupAddress = listOf(
                GroupAddress().apply {
                    groupShop = listOf(
                            GroupShop().apply {
                                shipmentInformationData = ShipmentInformationData(freeShipping = FreeShippingData(true, expectedBadgeUrl))
                                shop = Shop()
                            }
                    )
                    userAddress = UserAddress()
                }
        ))

        // When
        val result = shipmentDataConverter.getShipmentItems(cartShipmentAddressFormData, false)

        // Then
        assertEquals(expectedBadgeUrl, result.first().freeShippingBadgeUrl)
    }

    @Test
    fun `WHEN shop have no Bebas Ongkir THEN should use null as free shipping badge url`() {
        // Given
        val cartShipmentAddressFormData = CartShipmentAddressFormData(groupAddress = listOf(
                GroupAddress().apply {
                    groupShop = listOf(
                            GroupShop().apply {
                                shipmentInformationData = ShipmentInformationData()
                                shop = Shop()
                            }
                    )
                    userAddress = UserAddress()
                }
        ))

        // When
        val result = shipmentDataConverter.getShipmentItems(cartShipmentAddressFormData, false)

        // Then
        assertNull(result.first().freeShippingBadgeUrl)
    }
}