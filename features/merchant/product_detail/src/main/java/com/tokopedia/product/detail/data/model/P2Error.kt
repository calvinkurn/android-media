package com.tokopedia.product.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 20/07/20
 */
data class P2Error (
        @SerializedName("Code")
        @Expose
        var errorCode: Int = 0,
        @SerializedName("Message")
        @Expose
        var errorMessage: String = ""
)