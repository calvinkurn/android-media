package com.tokopedia.discovery2.data.automatecoupon

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

@SuppressLint("Invalid Data Type")
data class AutomateCouponRequest(
    @SerializedName("source")
    val source: String = String.EMPTY,
    @SerializedName("themeType")
    val theme: String = String.EMPTY,
    @SerializedName("widgetType")
    val widgetType: String,
    @SerializedName("ids")
    val ids: List<String>,
    @SerializedName("categoryIDs")
    val categoryIDs: List<String>,
    @SerializedName("subCategoryIDs")
    val subCategoryIDs: List<String>,
    @SerializedName("slugs")
    val slugs: List<String>,
)
