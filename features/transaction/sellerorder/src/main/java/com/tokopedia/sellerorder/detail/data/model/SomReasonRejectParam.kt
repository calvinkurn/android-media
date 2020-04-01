package com.tokopedia.sellerorder.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-11-05.
 */
data class SomReasonRejectParam (
        @SerializedName("lang")
        @Expose
        var lang: String = "id"
)