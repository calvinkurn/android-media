package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 13/07/22
 */
data class SetPinnedProduct(
    @SerializedName("success")
    val success: Boolean = false
)
