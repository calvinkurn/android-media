package com.tokopedia.feedplus.presentation.model

import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.feedplus.domain.mapper.MapperFeedModelToTrackerDataModel
import com.tokopedia.feedplus.presentation.model.type.AuthorType

/**
 * Created By : Muhammad Furqan on 18/04/23
 */
data class FeedTrackerDataModel(
    val activityId: String,
    val authorId: String,
    val tabType: String,
    val typename: String,
    val type: String,
    val authorType: AuthorType,
    val mediaType: String,
    val isFollowing: Boolean,
    val contentScore: String,
    val hasVoucher: Boolean,
    val campaignStatus: String,
    val entrySource: MapperFeedModelToTrackerDataModel.FeedEntrySource,
)

data class FeedTopAdsTrackerDataModel(
    val adViewUrl: String,
    val adClickUrl: String,
    val id: String,
    val shopId: String,
    val uri: String,
    val fullEcs: String?,
    val position: Int
)

data class FeedProductActionModel(
    val cartId: String,
    val product: ContentTaggedProductUiModel,
    val source: Source,
) {
    enum class Source {
        BottomSheet, CardHighlight,
    }
}
