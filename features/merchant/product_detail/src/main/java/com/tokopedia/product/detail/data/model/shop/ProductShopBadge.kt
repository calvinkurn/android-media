package com.tokopedia.product.detail.data.model.shop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 28/07/20
 */
data class ProductShopBadge(
        @SerializedName("badge")
        @Expose
        val badge: String = ""
)