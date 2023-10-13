package com.tokopedia.feedplus.browse.data

import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemUiModel
import com.tokopedia.feedplus.browse.data.model.FeedBrowseModel
import com.tokopedia.feedplus.browse.data.model.WidgetRecommendationModel
import com.tokopedia.feedplus.browse.presentation.model.FeedCategoryInspirationModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal interface FeedBrowseRepository {

    suspend fun getTitle(): String

    suspend fun getSlots(): List<FeedBrowseModel>

    suspend fun getWidgetContentSlot(extraParam: WidgetRequestModel): ContentSlotModel

    suspend fun getWidgetRecommendation(identifier: String): WidgetRecommendationModel

    suspend fun getCategoryInspiration(): List<FeedCategoryInspirationModel>



    suspend fun getWidget(extraParam: WidgetRequestModel): FeedBrowseItemUiModel
}
