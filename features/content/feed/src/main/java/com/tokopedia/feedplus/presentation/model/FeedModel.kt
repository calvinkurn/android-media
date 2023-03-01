package com.tokopedia.feedplus.presentation.model

/**
 * Created By : Shruti Agarwal on 10/02/23
 */
data class FeedModel(
    val items: List<FeedCardModel>,
    val pagination: FeedPaginationModel
)
