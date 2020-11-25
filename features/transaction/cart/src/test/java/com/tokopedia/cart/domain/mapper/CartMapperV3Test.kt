package com.tokopedia.cart.domain.mapper

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.cart.R
import com.tokopedia.cart.availableAndErrorCartItemMockData
import com.tokopedia.cart.availableCartItemMockData
import com.tokopedia.cart.errorCartItemMockData
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartDataListResponse
import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopGroupSimplifiedGqlResponse
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.domain.model.cartlist.CartTickerErrorData
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
                assertEquals(TickerData(0, "Hai Member Gold, kuota Bebas Ongkir kamu sisa 3x (untuk 1 pesanan/transaksi) buat minggu ini.", "cart"), result.tickerData)
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
})