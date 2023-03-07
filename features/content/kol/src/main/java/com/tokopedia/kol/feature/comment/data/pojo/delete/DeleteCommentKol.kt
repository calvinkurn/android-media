package com.tokopedia.kol.feature.comment.data.pojo.delete

import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 05/12/22
 */
data class DeleteCommentKol(
    @SerializedName("__typename")
    val typename: String = "",
    @SerializedName("error")
    val error: String = "",
    @SerializedName("data")
    val data: DeleteCommentKolData = DeleteCommentKolData()
)
