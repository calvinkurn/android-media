package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class FeedKolRecommendedType(
    @SerializedName("userName")
    @Expose val userName: String = "",

    @SerializedName("userPhoto")
    @Expose val userPhoto: String = "",

    @SerializedName("userId")
    @Expose val userId: String = "",

    @SerializedName("isFollowed")
    @Expose val isFollowed: Boolean = false,

    @SerializedName("info")
    @Expose val info: String = "",

    @SerializedName("url")
    @Expose val url: String = ""
)
