package com.tokopedia.kol.feature.comment.data.pojo.get

import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 05/12/22
 */
data class PostKol(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("headerTitle")
    val headerTitle: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("commentCount")
    val commentCount: Int = 0,
    @SerializedName("likeCount")
    val likeCount: Int = 0,
    @SerializedName("isLiked")
    val isLiked: Boolean = false,
    @SerializedName("isFollowed")
    val isFollowed: Boolean = false,
    @SerializedName("userName")
    val userName: String = "",
    @SerializedName("userPhoto")
    val userPhoto: String = "",
    @SerializedName("userId")
    val userId: String = "",
    @SerializedName("userInfo")
    val userInfo: String = "",
    @SerializedName("userUrl")
    val userUrl: String = "",
    @SerializedName("createTime")
    val createTime: String = "",
    @SerializedName("showComment")
    val showComment: Boolean = false,
    @SerializedName("content")
    val content: List<Content> = emptyList(),
    @SerializedName("userBadges")
    val userBadges: List<String> = emptyList(),
    @SerializedName("source")
    val source: Source = Source()
)
