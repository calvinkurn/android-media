package com.tokopedia.shop.common.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class HomeLayoutData(
    @SerializedName("layoutID")
    val layoutId: String = "",

    @SerializedName("masterLayoutID")
    val masterLayoutId: String = "",

    @SuppressLint("Invalid Data Type")
    @SerializedName("widgetIDList")
    val widgetIdList: List<WidgetIdList> = listOf(),
)