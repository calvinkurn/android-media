package com.tokopedia.kol.feature.comment.data.pojo.send

import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 05/12/22
 */
data class SendCommentKolGraphql(
    @SerializedName("create_comment_kol")
    val createCommentKol: SendCommentKol = SendCommentKol()
)
