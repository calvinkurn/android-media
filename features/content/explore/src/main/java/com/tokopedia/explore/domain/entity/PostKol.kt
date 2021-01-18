package com.tokopedia.explore.domain.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PostKol {
    @SerializedName("isLiked")
    @Expose
    var isIsLiked = false

    @SerializedName("isFollow")
    @Expose
    var isIsFollow = false

    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("commentCount")
    @Expose
    var commentCount = 0

    @SerializedName("likeCount")
    @Expose
    var likeCount = 0

    @SerializedName("createTime")
    @Expose
    var createTime: String = ""

    @SerializedName("description")
    @Expose
    var description: String = ""

    @SerializedName("content")
    @Expose
    var content: List<Content> = emptyList()

    @SerializedName("userName")
    @Expose
    var userName: String = ""

    @SerializedName("userInfo")
    @Expose
    var userInfo: String = ""

    @SerializedName("userIsFollow")
    @Expose
    var isUserIsFollow = false

    @SerializedName("userPhoto")
    @Expose
    var userPhoto: String = ""

    @SerializedName("userUrl")
    @Expose
    var userUrl: String = ""

    @SerializedName("userId")
    @Expose
    var userId = 0

    @SerializedName("tracking")
    @Expose
    val tracking: List<Tracking> = emptyList()

}