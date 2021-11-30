package com.tokopedia.product.detail.common.data.model.aggregator

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.product.Category

/**
 * Created by Yehezkiel on 07/07/21
 */
data class SimpleBasicInfo (
        @SerializedName("shopID")
        @Expose
        val shopID: String = "",
        @SerializedName("shopName")
        @Expose
        val shopName: String = "",
        @SerializedName("category")
        @Expose
        val category: Category = Category()
)