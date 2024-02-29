package com.tokopedia.discovery2.data.automatecoupon

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class CouponListWidgetFilter(
    @SerializedName("catalogAttributeFilter")
    var attributeFilter: AttributeFilter
)

@SuppressLint("Invalid Data Type")
data class AttributeFilter(
    @SerializedName("ids")
    var ids: List<String>,

    @SerializedName("categoryIDs")
    var categoryIDs: List<String>,

    @SerializedName("subCategoryIDs")
    var subCategoryIDs: List<String>,

    @SerializedName("slugs")
    var slugs: List<String>,
)
