package com.tokopedia.kol.feature.comment.data.pojo.get

import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 05/12/22
 */
data class GetKolCommentData(
    @SerializedName("get_user_post_comment") val getUserPostComment: GetUserPostComment = GetUserPostComment()
)
