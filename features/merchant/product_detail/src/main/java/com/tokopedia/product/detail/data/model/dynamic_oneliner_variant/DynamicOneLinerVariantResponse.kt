package com.tokopedia.product.detail.data.model.dynamic_oneliner_variant

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.data.model.dynamiconeliner.DynamicOneLiner

@SuppressLint("Invalid Data Type")
data class DynamicOneLinerVariantResponse(
    @SerializedName("productIDs")
    @Expose
    val productIds: List<String> = listOf(),

    @SerializedName("data")
    @Expose
    val dynamicOneLinerData: DynamicOneLiner = DynamicOneLiner()
)
