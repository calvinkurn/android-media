package com.tokopedia.minicart.common.widget.general.viewmodel.utils

import com.google.gson.Gson
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.mapper.MiniCartSimplifiedMapper
import com.tokopedia.minicart.common.widget.viewmodel.utils.UnitTestFileUtils

object DataProvider {

    private val gson = Gson()
    private val fileUtil = UnitTestFileUtils
    private val miniCartSimplifiedMapper = MiniCartSimplifiedMapper()

    fun provideGetMiniCartSimplifiedSuccessAllAvailable(): MiniCartSimplifiedData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_general_simplified_success_all_available"), MiniCartGqlResponse::class.java)
        return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(json.miniCart)
    }

    fun provideGetMiniCartSimplifiedSuccessSomeItemUnavailable(): MiniCartSimplifiedData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_general_simplified_success_some_unavailable"), MiniCartGqlResponse::class.java)
        return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(json.miniCart)
    }

    fun provideGetMiniCartSimplifiedSuccessAllUnavailable(): MiniCartSimplifiedData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_general_simplified_success_all_unavailable"), MiniCartGqlResponse::class.java)
        return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(json.miniCart)
    }

    fun provideGetMiniCartSimplifiedSuccessEmpty(): MiniCartSimplifiedData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_general_simplified_success_empty"), MiniCartGqlResponse::class.java)
        return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(json.miniCart)
    }

    fun provideGetMiniCartListSuccessAllAvailable(): MiniCartData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_general_success_all_available"), MiniCartGqlResponse::class.java)
        return json.miniCart
    }

    fun provideGetMiniCartListSuccessOutOfService(): MiniCartData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_general_success_out_of_service"), MiniCartGqlResponse::class.java)
        return json.miniCart
    }
}
