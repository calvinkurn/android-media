package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 06/11/20
 */

data class PostFilterModel(
        @Expose
        @SerializedName("name")
        val name: String? = "",
        @Expose
        @SerializedName("value")
        val value: String? = ""
)