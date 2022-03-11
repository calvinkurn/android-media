package com.tokopedia.addongifting.addonbottomsheet.view

import com.google.gson.Gson
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.GetAddOnByProductResponse
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonsavedstate.GetAddOnSavedStateResponse

object DataProvider {

    private val gson = Gson()
    private val fileUtil = UnitTestFileUtils

    fun provideLoadAddOnDataSuccess(): GetAddOnByProductResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_add_on_by_product_success"), GetAddOnByProductResponse::class.java)
    }

    fun provideLoadAddOnDataError(): GetAddOnByProductResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_add_on_by_product_error"), GetAddOnByProductResponse::class.java)
    }

    fun provideLoadAddOnSavedStateDataSuccess(): GetAddOnSavedStateResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_add_on_saved_state_success"), GetAddOnSavedStateResponse::class.java)
    }

    fun provideLoadAddOnSavedStateDataError(): GetAddOnSavedStateResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_add_on_saved_state_error"), GetAddOnSavedStateResponse::class.java)
    }

}