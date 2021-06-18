package com.tokopedia.minicart.common.widget.viewmodel.dataprovider

import com.google.gson.Gson
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.mapper.MiniCartSimplifiedMapper

object GetMiniCartListSimplifiedDataProvider {

    private val gson = Gson()
    private val dataMapper = MiniCartSimplifiedMapper()
    private val fileUtil = UnitTestFileUtils

    fun provideGetMiniCartSimplifiedSuccessAllAvailable(): MiniCartSimplifiedData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_simplified_success_all_available"), MiniCartGqlResponse::class.java)
        return dataMapper.mapMiniCartSimplifiedData(json.miniCart)
    }

    fun provideGetMiniCartSimplifiedSuccessAllUnavailable(): MiniCartSimplifiedData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_simplified_success_all_unavailable"), MiniCartGqlResponse::class.java)
        return dataMapper.mapMiniCartSimplifiedData(json.miniCart)
    }

    fun provideGetMiniCartSimplifiedSuccessEmptyData(): MiniCartSimplifiedData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_simplified_success_empty"), MiniCartGqlResponse::class.java)
        return dataMapper.mapMiniCartSimplifiedData(json.miniCart)
    }

}