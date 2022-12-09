package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class FeedPolling(
    @SerializedName("poll_id")
    @Expose val pollId: String = "",

    @SerializedName("title")
    @Expose val title: String = "",

    @SerializedName("description")
    @Expose val description: String = "",

    @SerializedName("question")
    @Expose val question: String = "",

    @SerializedName("commentCount")
    @Expose val commentCount: Int = 0,

    @SerializedName("likeCount")
    @Expose val likeCount: Int = 0,

    @SerializedName("createTime")
    @Expose val createTime: String = "",

    @SerializedName("liked")
    @Expose val liked: Boolean = false,

    @SerializedName("followed")
    @Expose val followed: Boolean = false,

    @SerializedName("showComment")
    @Expose val showComment: Boolean = false,

    @SerializedName("showLike")
    @Expose val showLike: Boolean = false,

    @SerializedName("totalVoter")
    @Expose val totalVoter: Int = 0,

    @SerializedName("isAnswered")
    @Expose val isAnswered: Boolean = false,

    @SerializedName("userName")
    @Expose val userName: String = "",

    @SerializedName("userPhoto")
    @Expose val userPhoto: String = "",

    @SerializedName("userId")
    @Expose val userId: String = "",

    @SerializedName("userInfo")
    @Expose val userInfo: String = "",

    @SerializedName("userUrl")
    @Expose val userUrl: String = "",

    @SerializedName("options")
    @Expose val options: List<PollingOption> = emptyList(),

    @SerializedName("relation")
    @Expose val relation: Relation = Relation()
)