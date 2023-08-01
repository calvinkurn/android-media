package com.tokopedia.cartrevamp.data.model.response.promo

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2020-03-05.
 */
data class PromoEmptyCartInfo(
    @SerializedName("image_url")
    var imageUrl: String = "",

    @SerializedName("message")
    var message: String = "",

    @SerializedName("detail")
    var detail: String = ""
)
