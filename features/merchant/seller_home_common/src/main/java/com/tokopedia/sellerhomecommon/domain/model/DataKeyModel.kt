package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 11/06/20
 */

data class DataKeyModel(
    @SerializedName("key") val key: String,
    @SerializedName("parameters") val jsonParams: String = "{}",

    @Transient val maxDisplay: Int = 0
)