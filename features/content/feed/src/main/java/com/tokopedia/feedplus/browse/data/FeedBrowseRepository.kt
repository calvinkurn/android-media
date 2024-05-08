package com.tokopedia.feedplus.browse.data

import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.FeedBrowseSlotUiModel
import com.tokopedia.feedplus.browse.data.model.HeaderDetailModel
import com.tokopedia.feedplus.browse.data.model.StoryGroupsModel
import com.tokopedia.feedplus.browse.data.model.WidgetRecommendationModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal interface FeedBrowseRepository {

    suspend fun getHeaderDetail(): HeaderDetailModel?

    suspend fun getCategoryInspirationTitle(source: String): String

    suspend fun getSlots(): List<FeedBrowseSlotUiModel>

    suspend fun getWidgetContentSlot(extraParam: WidgetRequestModel): ContentSlotModel

    suspend fun getWidgetRecommendation(identifier: String): WidgetRecommendationModel

    suspend fun getStoryGroups(source: String, cursor: String = ""): StoryGroupsModel

    suspend fun getUpdatedSeenStoriesStatus(
        shopId: String,
        currentHasSeenAll: Boolean,
        lastUpdated: Long
    ): Boolean
}
