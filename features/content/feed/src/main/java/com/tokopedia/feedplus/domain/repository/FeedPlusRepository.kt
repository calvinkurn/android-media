package com.tokopedia.feedplus.domain.repository

import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedplus.data.pojo.FeedTabs

/**
 * Created By : Jonathan Darwin on September 22, 2022
 */
interface FeedPlusRepository {

    suspend fun getWhitelist(): WhitelistQuery

    suspend fun getDynamicTabs(): FeedTabs

    suspend fun clearDynamicTabCache()
}
