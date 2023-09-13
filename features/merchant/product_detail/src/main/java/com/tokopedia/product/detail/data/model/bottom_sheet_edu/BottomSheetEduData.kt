package com.tokopedia.product.detail.data.model.bottom_sheet_edu

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by yovi.putra on 27/03/23"
 * Project name: android-tokopedia-core
 **/

data class BottomSheetEduData(
    @SerializedName("isShow")
    @Expose
    val isShow: Boolean = false,
    @SerializedName("appLink")
    @Expose
    val appLink: String = ""
)

fun BottomSheetEduData.asUiModel() = BottomSheetEduUiModel(
    isShow = isShow,
    appLink = appLink
)
