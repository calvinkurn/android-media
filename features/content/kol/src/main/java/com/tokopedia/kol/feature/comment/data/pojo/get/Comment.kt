package com.tokopedia.kol.feature.comment.data.pojo.get

import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 05/12/22
 */
data class Comment(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("userID")
    val userID: String = "",
    @SerializedName("userName")
    val userName: String = "",
    @SerializedName("userPhoto")
    val userPhoto: String = "",
    @SerializedName("isKol")
    val isKol: Boolean = false,
    @SerializedName("isCommentOwner")
    val isCommentOwner: Boolean = false,
    @SerializedName("create_time")
    val createTime: String = "",
    @SerializedName("comment")
    val comment: String = "",
    @SerializedName("userBadge")
    val userBadges: String = "",
    @SerializedName("isShop")
    val isShop: Boolean = false,
    @SerializedName("allowReport")
    val allowReport: Boolean = false
) {
}
