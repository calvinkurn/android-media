package com.tokopedia.addongifting.addonbottomsheet.view

import com.google.gson.Gson
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.GetAddOnByProductResponse

object DataProvider {

    private val gson = Gson()
    private val fileUtil = UnitTestFileUtils

    fun provideLoadAddOnDataSuccess(): GetAddOnByProductResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_add_on_success"), GetAddOnByProductResponse::class.java)
    }

    fun provideLoadAddOnDataError(): GetAddOnByProductResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_add_on_error"), GetAddOnByProductResponse::class.java)
    }

}