package com.tokopedia.cart.data.model.response.promo

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 07/03/20.
 */
data class ErrorDefault (
        @SerializedName("title")
        var title: String = "",

        @SerializedName("description")
        var desc: String = ""
)