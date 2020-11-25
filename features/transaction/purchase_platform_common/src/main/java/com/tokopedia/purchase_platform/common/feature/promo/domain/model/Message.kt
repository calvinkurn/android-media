package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

/**
 * Created by fwidjaja on 06/03/20.
 */

@Generated("com.robohorse.robopojogenerator")
data class Message(

        @field:SerializedName("color")
        val color: String? = null,

        @field:SerializedName("state")
        val state: String? = null,

        @field:SerializedName("text")
        val text: String? = null
)