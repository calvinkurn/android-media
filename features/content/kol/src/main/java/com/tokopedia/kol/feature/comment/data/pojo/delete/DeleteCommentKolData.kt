package com.tokopedia.kol.feature.comment.data.pojo.delete

import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 05/12/22
 */
data class DeleteCommentKolData(
    @SerializedName("__typename")
    val typename: String = "",
    @SerializedName("success")
    val success: Int = 0
)
