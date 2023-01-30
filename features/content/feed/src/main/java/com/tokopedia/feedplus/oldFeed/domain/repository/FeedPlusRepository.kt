package com.tokopedia.feedplus.oldFeed.domain.repository

import com.tokopedia.content.common.model.GetCheckWhitelistResponse
import com.tokopedia.feedplus.oldFeed.data.pojo.FeedTabs

/**
 * Created By : Jonathan Darwin on September 22, 2022
 */
interface FeedPlusRepository {

    suspend fun getWhitelist(): GetCheckWhitelistResponse

    suspend fun getDynamicTabs(): FeedTabs

    suspend fun clearDynamicTabCache()
}
