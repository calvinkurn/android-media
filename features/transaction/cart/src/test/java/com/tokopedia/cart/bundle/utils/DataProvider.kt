package com.tokopedia.cart.bundle.utils

import com.google.gson.Gson
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartGqlResponse
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data

object DataProvider {

    private val gson = Gson()
    private val fileUtil = UnitTestFileUtils

    fun provideUpdateCartSuccess(): UpdateCartV2Data {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/update_cart_success"), UpdateCartGqlResponse::class.java)
        return json.updateCartData
    }

    fun provideUpdateCartFailed(): UpdateCartV2Data {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/update_cart_failed"), UpdateCartGqlResponse::class.java)
        return json.updateCartData
    }

}