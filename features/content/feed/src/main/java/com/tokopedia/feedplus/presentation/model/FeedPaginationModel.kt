package com.tokopedia.feedplus.presentation.model

/**
 * Created By : Muhammad Furqan on 28/02/23
 */
data class FeedPaginationModel(
    var cursor: String,
    val hasNext: Boolean,
    var totalData: Int
)
