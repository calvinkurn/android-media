package com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 13/03/20.
 */
data class EmptyCartInfo(
    @field:SerializedName("image_url")
    val imageUrl: String = "",

    @field:SerializedName("message")
    val message: String = "",

    @field:SerializedName("detail")
    val detail: String = ""
)
