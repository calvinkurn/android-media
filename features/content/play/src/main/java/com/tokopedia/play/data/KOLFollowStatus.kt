package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 02/03/22
 */

data class FollowInfo(
    @SerializedName("data")
    val data: Data = Data(),

    @SerializedName("messages")
    val message: List<String> = emptyList(),

    @SerializedName("error_code")
    val errorCode: String = "",
){
    data class Data(
        @SerializedName("is_success")
        val isSuccess: Boolean = false,
    )
}

data class KOLFollowStatus(
    @SerializedName("SocialNetworkFollow")
    val followedKOLInfo: FollowInfo = FollowInfo()
)

data class KOLUnFollowStatus(
    @SerializedName("SocialNetworkUnfollow")
    val unFollowedKOLInfo: FollowInfo = FollowInfo()
)
