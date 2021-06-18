package com.tokopedia.minicart.common.widget.viewmodel.dataprovider

import com.google.gson.Gson
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse

object GetMiniCartListDataProvider {

    private val gson = Gson()
    private val fileUtil = UnitTestFileUtils

    fun provideGetMiniCartListSuccessAllAvailable(): MiniCartData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_success_all_available"), MiniCartGqlResponse::class.java)
        return json.miniCart
    }

    fun provideGetMiniCartListSuccessAllUnavailable(): MiniCartData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_success_all_unavailable"), MiniCartGqlResponse::class.java)
        return json.miniCart
    }

    fun provideGetMiniCartListSuccessOutOfService(): MiniCartData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_success_out_of_service"), MiniCartGqlResponse::class.java)
        return json.miniCart
    }

}