package com.tokopedia.purchase_platform.features.cart.domain.mapper

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.apiResponseAllShopWithWholeSaleJson
import com.tokopedia.purchase_platform.apiResponseAvailableShopJson
import com.tokopedia.purchase_platform.apiResponseShopErrorJson
import com.tokopedia.purchase_platform.common.feature.ticker_announcement.TickerData
import com.tokopedia.purchase_platform.features.cart.data.model.response.CartDataListResponse
import com.tokopedia.purchase_platform.features.cart.data.model.response.ShopGroupSimplifiedGqlResponse
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartTickerErrorData
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class CartMapperV3Test : Spek({

    val context = mockk<Context>(relaxed = true)
    val cartMapperV3 by memoized { CartSimplifiedMapper(context) }

    every { context.getString(R.string.cart_error_message) } returns "Ada %d barang yang tidak dapat dibeli"
    every { context.getString(R.string.cart_error_action) } returns "Hapus Produk Bermasalah"

    Feature("convert cart data list") {

        lateinit var cartDataListResponse: CartDataListResponse
        lateinit var result: CartListData

        Scenario("available shops with no error") {

            Given("api response") {
                cartDataListResponse = Gson().fromJson(apiResponseAvailableShopJson, ShopGroupSimplifiedGqlResponse::class.java).shopGroupSimplifiedResponse.data
            }

            When("map response") {
                result = cartMapperV3.convertToCartItemDataList(cartDataListResponse)
            }

            Then("should not error") {
                assertEquals(result.isError, false)
            }

            Then("should contains ticker data") {
                assertEquals(TickerData(0, "Pesanan di keranjangmu berpotensi dapat Bebas Ongkir", "cart"), result.tickerData)
            }

            Then("should contains 1 available shop") {
                assertEquals(1, result.shopGroupAvailableDataList.size)
            }

            Then("should contains 0 error shop") {
                assertEquals(0, result.shopGroupWithErrorDataList.size)
            }
        }

        Scenario("error shops with no available") {

            Given("api response") {
                cartDataListResponse = Gson().fromJson(apiResponseShopErrorJson, ShopGroupSimplifiedGqlResponse::class.java).shopGroupSimplifiedResponse.data
            }

            When("map response") {
                result = cartMapperV3.convertToCartItemDataList(cartDataListResponse)
            }

            Then("should error") {
                assertEquals(result.isError, true)
            }

            Then("should contains ticker error data with 1 error count") {
                val cartTickerErrorData = CartTickerErrorData()
                cartTickerErrorData.errorCount = 1
                cartTickerErrorData.errorInfo = String.format(context.getString(R.string.cart_error_message), 1)
                cartTickerErrorData.actionInfo = context.getString(R.string.cart_error_action)
                assertEquals(cartTickerErrorData, result.cartTickerErrorData)
            }

            Then("should contains 0 available shop") {
                assertEquals(0, result.shopGroupAvailableDataList.size)
            }

            Then("should contains 1 error shop") {
                assertEquals(1, result.shopGroupWithErrorDataList.size)
            }
        }

        Scenario("available & error shops") {

            Given("api response") {
                cartDataListResponse = Gson().fromJson(apiResponseAllShopWithWholeSaleJson, ShopGroupSimplifiedGqlResponse::class.java).shopGroupSimplifiedResponse.data
            }

            When("map response") {
                result = cartMapperV3.convertToCartItemDataList(cartDataListResponse)
            }

            Then("should error") {
                assertEquals(result.isError, true)
            }

            Then("should contains ticker error data with 1 error count") {
                val cartTickerErrorData = CartTickerErrorData()
                cartTickerErrorData.errorCount = 1
                cartTickerErrorData.errorInfo = String.format(context.getString(R.string.cart_error_message), 1)
                cartTickerErrorData.actionInfo = context.getString(R.string.cart_error_action)
                assertEquals(cartTickerErrorData, result.cartTickerErrorData)
            }

            Then("should contains 2 available shops") {
                assertEquals(2, result.shopGroupAvailableDataList.size)
            }

            Then("should contains 1 error shop") {
                assertEquals(1, result.shopGroupWithErrorDataList.size)
            }
        }

    }
})