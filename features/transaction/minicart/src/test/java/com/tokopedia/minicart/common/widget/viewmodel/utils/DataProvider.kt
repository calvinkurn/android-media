package com.tokopedia.minicart.common.widget.viewmodel.utils

import com.google.gson.Gson
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.mapper.MiniCartSimplifiedMapper

object DataProvider {

    private val gson = Gson()
    private val fileUtil = UnitTestFileUtils
    private val miniCartSimplifiedMapper = MiniCartSimplifiedMapper()
    private val miniCartListUiModelMapper = MiniCartListUiModelMapper()

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

    fun provideGetMiniCartSimplifiedSuccessAllAvailable(): MiniCartSimplifiedData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_simplified_success_all_available"), MiniCartGqlResponse::class.java)
        return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(json.miniCart)
    }

    fun provideGetMiniCartSimplifiedSuccessAllUnavailable(): MiniCartSimplifiedData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_simplified_success_all_unavailable"), MiniCartGqlResponse::class.java)
        return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(json.miniCart)
    }

    fun provideGetMiniCartSimplifiedSuccessEmptyData(): MiniCartSimplifiedData {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_simplified_success_empty"), MiniCartGqlResponse::class.java)
        return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(json.miniCart)
    }

    fun provideMiniCartListUiModelAllAvailable(): MiniCartListUiModel {
        val miniCartData = provideGetMiniCartListSuccessAllAvailable()
        return miniCartListUiModelMapper.mapUiModel(miniCartData)
    }

    fun provideMiniCartListUiModelAllUnavailable(): MiniCartListUiModel {
        val miniCartData = provideGetMiniCartListSuccessAllUnavailable()
        return miniCartListUiModelMapper.mapUiModel(miniCartData)
    }

    fun provideMiniCartSimplifiedDataAllAvailable(): MiniCartSimplifiedData {
        val miniCartSimplifiedData = provideGetMiniCartSimplifiedSuccessAllAvailable()
        return miniCartSimplifiedData
    }

    fun provideMiniCartSimplifiedDataAllUnavailable(): MiniCartSimplifiedData {
        val miniCartSimplifiedData = provideGetMiniCartSimplifiedSuccessAllUnavailable()
        return miniCartSimplifiedData
    }

    fun provideGetMiniCartListSuccessWithWholesaleVariant(): MiniCartListUiModel {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_success_wholesale_variant"), MiniCartGqlResponse::class.java)
        return miniCartListUiModelMapper.mapUiModel(json.miniCart)
    }

    fun provideGetMiniCartListSuccessWithSlashPrice(): MiniCartListUiModel {
        val json = gson.fromJson(fileUtil.getJsonFromAsset("assets/get_mini_cart_success_slash_price"), MiniCartGqlResponse::class.java)
        return miniCartListUiModelMapper.mapUiModel(json.miniCart)
    }

}