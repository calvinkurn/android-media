package com.tokopedia.product.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 20/07/20
 */
data class P2Error (
        @SerializedName("error")
        @Expose
        var isError: Boolean = false,
        @SerializedName("code")
        @Expose
        var errorCode: String = "",
        @SerializedName("errorMsg")
        @Expose
        var errorMessage: String = ""
)