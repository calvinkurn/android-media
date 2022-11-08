package com.tokopedia.feedcomponent.people.model

import com.google.gson.annotations.SerializedName

data class ProfileDoFollowModelBase(
    @SerializedName("SocialNetworkFollow")
    val profileFollowers: ProfileDoFollowedData,
)

data class ProfileDoUnFollowModelBase(
    @SerializedName("SocialNetworkUnfollow")
    val profileFollowers: ProfileDoFollowedData,
)

data class ProfileDoFollowedData(
    @SerializedName("data")
    val data: ProfileDoFollowedDataVal,

    @SerializedName("messages")
    val messages: List<String>,

    @SerializedName("error_code")
    val errorCode: String
)

data class ProfileDoFollowedDataVal(
    @SerializedName("user_id_source")
    val userIdSource: String,

    @SerializedName("user_id_target")
    val userIdTarget: String,

    @SerializedName("relation")
    val relation: String,

    @SerializedName("is_success")
    val isSuccess: String,
)



