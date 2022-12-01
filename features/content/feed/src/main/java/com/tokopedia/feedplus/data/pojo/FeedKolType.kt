package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class FeedKolType(
    @SerializedName("id")
    @Expose
    val id: String = "",

    @SerializedName("headerTitle")
    @Expose
    val headerTitle: String = "",

    @SerializedName("description")
    @Expose
    val description: String = "",

    @SerializedName("commentCount")
    @Expose
    val commentCount: Int = 0,

    @SerializedName("likeCount")
    @Expose
    val likeCount: Int = 0,

    @SerializedName("showComment")
    @Expose
    val showComment: Boolean = false,

    @SerializedName("showLike")
    @Expose
    val showLike: Boolean = false,

    @SerializedName("isLiked")
    @Expose
    val isLiked: Boolean = false,

    @SerializedName("isFollowed")
    @Expose
    val isFollowed: Boolean = false,

    @SerializedName("userName")
    @Expose
    val userName: String = "",

    @SerializedName("userPhoto")
    @Expose
    val userPhoto: String = "",

    @SerializedName("userId")
    @Expose
    val userId: String = "",

    @SerializedName("userInfo")
    @Expose
    val userInfo: String = "",

    @SerializedName("userUrl")
    @Expose
    val userUrl: String = "",

    @SerializedName("createTime")
    @Expose
    val createTime: String = "",

    @SerializedName("content")
    @Expose
    val content: List<ContentFeedKol> = emptyList()
)
