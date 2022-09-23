package com.tokopedia.feedplus.domain.repository

import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * Created By : Jonathan Darwin on September 22, 2022
 */
interface FeedPlusRepository {

    suspend fun getWhitelist(): WhitelistQuery

    suspend fun getDynamicTabs(): FeedTabs

    suspend fun clearDynamicTabCache()

    fun getFeedContentForm(subscriber: Subscriber<GraphqlResponse>)
}
