package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 02/03/22
 */
data class ProfileHeader(
    @SerializedName("profile")
    val profileInfo: ProfileInfo = ProfileInfo()
){
    data class Response(
        @SerializedName("feedXProfileHeader")
        val response: ProfileHeader = ProfileHeader()
    )

    data class ProfileInfo(
        @SerializedName("encryptedUserID")
        val encryptedUserId: String = "",

        @SerializedName("nickname")
        val nickname: String = "",

        @SerializedName("username")
        val username: String = "",
    )
}
