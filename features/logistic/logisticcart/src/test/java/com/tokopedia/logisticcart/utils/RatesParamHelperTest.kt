package com.tokopedia.logisticcart.utils

import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.ShipProd
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import org.junit.Assert.assertEquals
import org.junit.Test

class RatesParamHelperTest {

    @Test
    fun `given one spids when generate spids return only one spids`() {
        val data = listOf(ShopShipment().apply { shipProds = listOf(ShipProd().apply { shipProdId = 1 }) })
        val expected = "1"
        val actual = RatesParamHelper.generateSpIds(data)
        assertEquals(expected, actual)
    }

    @Test
    fun `given multiple spids when generate spids return multiple spids with commas`() {
        val data = listOf(
                ShopShipment().apply {
                    shipProds = listOf(
                            ShipProd().apply {
                                shipProdId = 1
                            },
                            ShipProd().apply {
                                shipProdId = 2
                            }
                    )
                },
                ShopShipment().apply {
                    shipProds = listOf(
                            ShipProd().apply {
                                shipProdId = 3
                            },
                            ShipProd().apply {
                                shipProdId = 16
                            }
                    )
                }
        )
        val expected = "1,2,3,16"

        val actual = RatesParamHelper.generateSpIds(data)
        assertEquals(expected, actual)
    }

    @Test
    fun `given shipping param when generate origin return format with pipeline symbol`() {
        val data = ShippingParam().apply {
            originDistrictId = "2270"
            originPostalCode = "12930"
            originLatitude = "-6.22184"
            originLongitude = "106.8194"
        }
        val expected = "2270|12930|-6.22184,106.8194"

        val actual = RatesParamHelper.generateOrigin(data)
        assertEquals(expected, actual)
    }

    @Test
    fun `given shipping param when generate destination return format with pipeline symbol`() {
        val data = ShippingParam().apply {
            destinationDistrictId = "2253"
            destinationPostalCode = "11720"
            destinationLatitude = "-6.1486"
            destinationLongitude = "106.735185"
        }
        val expected = "2253|11720|-6.1486,106.735185"

        val actual = RatesParamHelper.generateDestination(data)
        assertEquals(expected, actual)
    }

    @Test
    fun `given tradein dropoff enabled return 2`() {
        val data = ShippingParam().apply {
            isTradeInDropOff = true
            isTradein = false
        }
        val expected = 2

        val actual = RatesParamHelper.determineTradeIn(data)
        assertEquals(expected, actual)
    }

    @Test
    fun `given one product model when generate product json return json string`() {
        val data = ShippingParam().apply {
            products = listOf(
                    Product(298851813, isFreeShipping = false, isFreeShippingTc = false)
            )
        }
        val expected = "[{\"product_id\":298851813,\"is_free_shipping\":false,\"is_free_shipping_tc\":false}]"
        val actual = RatesParamHelper.generateProducts(data)
        assertEquals(expected, actual)
    }
}