package com.tokopedia.feedplus.browse.data

import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.FeedBrowseSlotUiModel
import com.tokopedia.feedplus.browse.data.model.WidgetRecommendationModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal interface FeedBrowseRepository {

    suspend fun getTitle(): String

    suspend fun getCategoryInspirationTitle(source: String): String

    suspend fun getSlots(): List<FeedBrowseSlotUiModel>

    suspend fun getWidgetContentSlot(extraParam: WidgetRequestModel): ContentSlotModel

    suspend fun getWidgetRecommendation(identifier: String): WidgetRecommendationModel
}
