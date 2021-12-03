package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 06/03/20.
 */

data class Message(
        @field:SerializedName("color")
        val color: String = "",
        @field:SerializedName("state")
        val state: String = "",
        @field:SerializedName("text")
        val text: String = ""
)