package com.tokopedia.kol.feature.comment.data.pojo.send

import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 05/12/22
 */
data class SendCommentKol(
    @SerializedName("__typename")
    val typename: String = "",
    @SerializedName("error")
    val error: String = "",
    @SerializedName("data")
    val data: SendCommentKolData = SendCommentKolData()
)
