package com.tokopedia.feedplus.browse.data

import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseSlot
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
interface FeedBrowseRepository {

    suspend fun getTitle(): String

    suspend fun getSlots(): List<FeedBrowseSlot>

    suspend fun getWidgets(type: String): List<FeedBrowseUiModel>
}
