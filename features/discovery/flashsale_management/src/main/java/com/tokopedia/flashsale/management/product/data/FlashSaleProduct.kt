package com.tokopedia.flashsale.management.product.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 25/10/18.
 */
data class FlashSaleProduct(
        @SerializedName("id")
        @Expose val id: String = "",

        @SerializedName("shop_id")
        @Expose val shopId: String = ""
)