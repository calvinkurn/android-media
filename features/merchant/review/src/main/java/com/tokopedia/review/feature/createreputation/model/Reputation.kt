package com.tokopedia.review.feature.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Reputation(
    @SerializedName("score")
    @Expose
    val score: Int = 0,
    @SerializedName("locked")
    @Expose
    val locked: Boolean = false,
    @SerializedName("filled")
    @Expose
    val filled: Boolean = false
) : Serializable