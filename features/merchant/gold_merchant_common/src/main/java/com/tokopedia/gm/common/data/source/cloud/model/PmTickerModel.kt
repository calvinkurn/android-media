package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 08/03/21
 */

data class PmTickerModel(
        @SerializedName("title")
        val title: String? = "",
        @SerializedName("text")
        val text: String? = "",
        @SerializedName("type")
        val type: String? = ""
)