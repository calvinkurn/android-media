package com.tokopedia.kol.feature.comment.data.pojo.send

import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 05/12/22
 */
data class SendCommentKolUser(
    @SerializedName("__typename")
    val typename: String = "",
    @SerializedName("iskol")
    val iskol: Boolean = false,
    @SerializedName("id")
    val id: String = "0",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("photo")
    val photo: String = ""
)
