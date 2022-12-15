package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class Relation(
    @SerializedName("isFollowed")
    @Expose val isFollowed: Boolean = false,

    @SerializedName("isLiked")
    @Expose val isLiked: Boolean = false
)
