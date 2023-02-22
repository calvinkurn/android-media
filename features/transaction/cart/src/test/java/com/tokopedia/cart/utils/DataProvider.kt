package com.tokopedia.cart.utils

import com.google.gson.Gson
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartGqlResponse
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data

object DataProvider {

    private val gson = Gson()

    fun provideUpdateCartSuccess(): UpdateCartV2Data {
        val json = gson.fromJson(UnitTestFileUtils.getJsonFromAsset("assets/update_cart_success"), UpdateCartGqlResponse::class.java)
        return json.updateCartData
    }

}
