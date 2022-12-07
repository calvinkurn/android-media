package com.tokopedia.kol.feature.comment.data.pojo.send

import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 05/12/22
 */
data class SendCommentKolData(
    @SerializedName("__typename")
    val typename: String = "",
    @SerializedName("id")
    val id: String = "0",
    @SerializedName("user")
    val user: SendCommentKolUser = SendCommentKolUser(),
    @SerializedName("comment")
    val comment: String = "",
    @SerializedName("create_time")
    val createTime: String = ""
)
