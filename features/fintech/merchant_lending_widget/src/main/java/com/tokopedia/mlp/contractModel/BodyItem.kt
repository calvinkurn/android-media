package com.tokopedia.mlp.contractModel

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class BodyItem(

        @SerializedName("boxes")
        val boxes: List<BoxesItem?>? = null,

        @SerializedName("info")
        val info: List<InfoItem?>? = null,

        @SerializedName("url")
        val urlbody: String? = null


)
