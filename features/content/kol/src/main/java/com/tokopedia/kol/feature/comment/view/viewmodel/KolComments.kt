package com.tokopedia.kol.feature.comment.view.viewmodel

/**
 * @author by nisie on 10/31/17.
 */
data class KolComments(
    val lastCursor: String,
    val isHasNextPage: Boolean,
    val listNewComments: List<KolCommentNewModel>,
    val headerNewModel: KolCommentHeaderNewModel,
)
