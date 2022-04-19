package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 08/03/21
 */

data class PmTickerModel(
        @SerializedName("title")
        @Expose
        val title: String? = "",
        @SerializedName("text")
        @Expose
        val text: String? = "",
        @SerializedName("type")
        @Expose
        val type: String? = ""
)