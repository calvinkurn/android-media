package com.tokopedia.feedplus.browse.data

import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
interface FeedBrowseRepository {

    suspend fun getTitle(): String

    suspend fun getSlots(): List<FeedBrowseUiModel>

    suspend fun getWidget(extraParam: WidgetRequestModel): FeedBrowseItemUiModel
}
