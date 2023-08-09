package com.tokopedia.feedplus.domain.repository

import com.tokopedia.feedplus.presentation.model.FeedTabsModel
import com.tokopedia.feedplus.presentation.model.MetaModel

/**
 * Created by meyta.taliti on 08/08/23.
 */
interface FeedRepository {

    suspend fun getTabs(): FeedTabsModel

    suspend fun getMeta(): MetaModel
}
