package com.tokopedia.cart.domain.mapper

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.cart.R
import com.tokopedia.cart.availableAndErrorCartItemMockData
import com.tokopedia.cart.availableCartItemMockData
import com.tokopedia.cart.data.model.response.shopgroupsimplified.*
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.domain.model.cartlist.CartTickerErrorData
import com.tokopedia.cart.errorCartItemMockData
import com.tokopedia.purchase_platform.common.feature.fulfillment.response.TokoCabangInfo
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object CartMapperV3Test : Spek({

    val context = mockk<Context>(relaxed = true)
    val cartMapperV3 by memoized { CartSimplifiedMapper(context) }

    every { context.getString(R.string.cart_error_message) } returns "Ada %d barang yang tidak dapat dibeli"
    every { context.getString(R.string.cart_error_action) } returns "Hapus Produk Bermasalah"

    Feature("convert cart data list") {

        lateinit var cartDataListResponse: CartDataListResponse
        lateinit var result: CartListData

        Scenario("available shops with no error") {

            Given("api response") {
                cartDataListResponse = Gson().fromJson(availableCartItemMockData, ShopGroupSimplifiedGqlResponse::class.java).shopGroupSimplifiedResponse.data
            }

            When("map response") {
                result = cartMapperV3.convertToCartItemDataList(cartDataListResponse)
            }

            Then("should not error") {
                assertEquals(result.isError, false)
            }

            Then("should contains ticker data") {
                assertEquals(TickerData("0", "Hai Member Gold, kuota Bebas Ongkir kamu sisa 3x (untuk 1 pesanan/transaksi) buat minggu ini.", "cart"), result.tickerData)
            }

            Then("should contains 1 available shop") {
                assertEquals(1, result.shopGroupAvailableDataList.size)
            }

            Then("should contains 0 error shop") {
                assertEquals(0, result.unavailableGroupData.size)
            }
        }

        Scenario("error shops with no available") {

            Given("api response") {
                cartDataListResponse = Gson().fromJson(errorCartItemMockData, ShopGroupSimplifiedGqlResponse::class.java).shopGroupSimplifiedResponse.data
            }

            When("map response") {
                result = cartMapperV3.convertToCartItemDataList(cartDataListResponse)
            }

            Then("should error") {
                assertEquals(result.isError, true)
            }

            Then("should contains ticker error data with 1 error count") {
                val cartTickerErrorData = CartTickerErrorData(errorCount = 1)
                assertEquals(cartTickerErrorData.errorCount, 1)
            }

            Then("should contains 0 available shop") {
                assertEquals(0, result.shopGroupAvailableDataList.size)
            }

            Then("should contains 1 error shop") {
                assertEquals(1, result.unavailableGroupData.size)
            }
        }

        Scenario("available & error shops") {

            Given("api response") {
                cartDataListResponse = Gson().fromJson(availableAndErrorCartItemMockData, ShopGroupSimplifiedGqlResponse::class.java).shopGroupSimplifiedResponse.data
            }

            When("map response") {
                result = cartMapperV3.convertToCartItemDataList(cartDataListResponse)
            }

            Then("should error") {
                assertEquals(result.isError, true)
            }

            Then("should contains ticker error data with 1 error count") {
                val cartTickerErrorData = CartTickerErrorData(errorCount = 1)
                assertEquals(cartTickerErrorData.errorCount, 1)
            }

            Then("should contains 2 available shops") {
                assertEquals(1, result.shopGroupAvailableDataList.size)
            }

            Then("should contains 1 error shop") {
                assertEquals(1, result.unavailableGroupData.size)
            }
        }
    }

    Feature("Bebas Ongkir Extra") {

        lateinit var cartDataListResponse: CartDataListResponse
        lateinit var result: CartListData

        val freeShippingBadgeUrl = "bebas.ongkir.com"
        val freeShippingExtraBadgeUrl = "bebas.ongkir.extra.com"

        Scenario("Shop is has no Bebas Ongkir") {

            Given("Shop with no Bebas Ongkir") {
                cartDataListResponse = CartDataListResponse(
                        availableSection = AvailableSection(
                                availableGroupGroups = listOf(
                                        AvailableGroup(
                                                shipmentInformation = ShipmentInformation()
                                        )
                                )
                        )
                )
            }

            When("map response") {
                result = cartMapperV3.convertToCartItemDataList(cartDataListResponse)
            }

            Then("should have empty string as free shipping badge url") {
                assertEquals("", result.shopGroupAvailableDataList.first().freeShippingBadgeUrl)
            }
        }

        Scenario("Shop have Bebas Ongkir") {

            Given("Shop have Bebas Ongkir") {
                cartDataListResponse = CartDataListResponse(
                        availableSection = AvailableSection(
                                availableGroupGroups = listOf(
                                        AvailableGroup(
                                                shipmentInformation = ShipmentInformation(
                                                        freeShipping = FreeShipping(true, freeShippingBadgeUrl)
                                                )
                                        )
                                )
                        )
                )
            }

            When("map response") {
                result = cartMapperV3.convertToCartItemDataList(cartDataListResponse)
            }

            Then("should have bebas ongkir url as free shipping badge url") {
                assertEquals(freeShippingBadgeUrl, result.shopGroupAvailableDataList.first().freeShippingBadgeUrl)
            }
        }

        Scenario("Shop have Bebas Ongkir Extra") {

            Given("Shop have Bebas Ongkir Extra") {
                cartDataListResponse = CartDataListResponse(
                        availableSection = AvailableSection(
                                availableGroupGroups = listOf(
                                        AvailableGroup(
                                                shipmentInformation = ShipmentInformation(
                                                        freeShippingExtra = FreeShipping(true, freeShippingExtraBadgeUrl)
                                                )
                                        )
                                )
                        )
                )
            }

            When("map response") {
                result = cartMapperV3.convertToCartItemDataList(cartDataListResponse)
            }

            Then("should have bebas ongkir extra url as free shipping badge url") {
                assertEquals(freeShippingExtraBadgeUrl, result.shopGroupAvailableDataList.first().freeShippingBadgeUrl)
            }
        }
    }

    Feature("Fulfillment") {

        lateinit var cartDataListResponse: CartDataListResponse
        lateinit var result: CartListData

        val tokoCabangResponse = TokoCabangInfo("tokocabang", "img.tokocabang.com")
        val shopLocation = "shop location"

        Scenario("Shop is not fulfillment") {

            Given("Not Fulfillment Shop") {
                cartDataListResponse = CartDataListResponse(
                        availableSection = AvailableSection(
                                availableGroupGroups = listOf(
                                        AvailableGroup(
                                                isFulFillment = false,
                                                shipmentInformation = ShipmentInformation(
                                                        shopLocation = shopLocation
                                                )
                                        )
                                )
                        ),
                        tokoCabangInfo = tokoCabangResponse
                )
            }

            When("map response") {
                result = cartMapperV3.convertToCartItemDataList(cartDataListResponse)
            }

            Then("should have isFulfillment false") {
                assertEquals(false, result.shopGroupAvailableDataList.first().isFulfillment)
            }

            Then("should have shop location as fulfillment name") {
                assertEquals(shopLocation, result.shopGroupAvailableDataList.first().fulfillmentName)
            }
        }

        Scenario("Shop is fulfillment") {

            Given("Fulfillment Shop") {
                cartDataListResponse = CartDataListResponse(
                        availableSection = AvailableSection(
                                availableGroupGroups = listOf(
                                        AvailableGroup(
                                                isFulFillment = true,
                                                shipmentInformation = ShipmentInformation(
                                                        shopLocation = shopLocation
                                                )
                                        )
                                )
                        ),
                        tokoCabangInfo = tokoCabangResponse
                )
            }

            When("map response") {
                result = cartMapperV3.convertToCartItemDataList(cartDataListResponse)
            }

            Then("should have isFulfillment true") {
                assertEquals(true, result.shopGroupAvailableDataList.first().isFulfillment)
            }

            Then("should have tokocabang as fulfillment name") {
                assertEquals(tokoCabangResponse.message, result.shopGroupAvailableDataList.first().fulfillmentName)
            }

            Then("should have tokocabang badge url as fulfillment badge url") {
                assertEquals(tokoCabangResponse.badgeUrl, result.shopGroupAvailableDataList.first().fulfillmentBadgeUrl)
            }
        }
    }
})