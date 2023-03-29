package com.tokopedia.feedplus.presentation.model

import com.tokopedia.feedplus.presentation.util.common.FeedLikeAction


/**
 * Created by shruti.agarwal on 10/03/23.
 */
data class LikeFeedDataModel(
    val rowNumber: Int,
    val action: FeedLikeAction,
)
