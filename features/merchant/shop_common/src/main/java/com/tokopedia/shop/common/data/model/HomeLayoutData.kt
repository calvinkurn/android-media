package com.tokopedia.shop.common.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeLayoutData(
    @SerializedName("layoutID")
    @Expose
    val layoutId: String = "",

    @SerializedName("masterLayoutID")
    @Expose
    val masterLayoutId: String = "",

    @SuppressLint("Invalid Data Type")
    @SerializedName("widgetIDList")
    @Expose
    val widgetIdList: List<WidgetIdList> = listOf()
)