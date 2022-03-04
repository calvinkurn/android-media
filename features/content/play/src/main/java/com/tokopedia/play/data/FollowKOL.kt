package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 02/03/22
 */
data class FollowKOL(
    @SerializedName("isUserFollowing")
    val followedKOLInfo: List<FollowedKOL> = emptyList()
){
    data class Response(
        @SerializedName("feedXProfileIsFollowing")
        val response: FollowKOL = FollowKOL()
    )

    data class FollowedKOL(
        @SerializedName("userID")
        val kolId: String = "",

        @SerializedName("status")
        val status: Boolean = false,
    )
}