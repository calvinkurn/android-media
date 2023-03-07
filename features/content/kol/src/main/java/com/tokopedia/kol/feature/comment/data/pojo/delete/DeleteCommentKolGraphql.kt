package com.tokopedia.kol.feature.comment.data.pojo.delete

import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 05/12/22
 */
data class DeleteCommentKolGraphql(
    @SerializedName("delete_comment_kol")
    val deleteCommentKol: DeleteCommentKol
)
