package com.tokopedia.transactiondata.entity.response.expresscheckoutform.variant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class Options(

        @SerializedName("id")
        @Expose
        val id: Int,

        @SerializedName("value")
        @Expose
        val value: String,

        @SerializedName("hex")
        @Expose
        val hex: String

)