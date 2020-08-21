package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 11/06/20
 */

data class DataKeyModel(
        @Expose
        @SerializedName("key")
        val key: String,
        @Expose
        @SerializedName("parameters")
        val jsonParams: String
)