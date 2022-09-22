package com.tokopedia.feedplus.domain.repository

import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery

/**
 * Created By : Jonathan Darwin on September 22, 2022
 */
interface FeedPlusRepository {

    suspend fun getWhitelist(): WhitelistQuery
}
