package com.tokopedia.tkpd.feed_component.fake

import com.tokopedia.feedplus.domain.repository.FeedPlusRepository
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.data.pojo.whitelist.Author
import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * Created By : Jonathan Darwin on September 23, 2022
 */
class FakeFeedPlusRepository : FeedPlusRepository {

    private var whitelistResponse = WhitelistQuery(
        whitelist = Whitelist(
            authors = listOf(
                Author(
                    type = "content-user",
                    thumbnail = "https://images.tokopedia.net/img/cache/100-square/default_picture_user/default_toped-24.jpg"
                )
            )
        )
    )

    private var dynamicTabsResponse: FeedTabs = FeedTabs(
        feedData = listOf(
            FeedTabs.FeedData(
                isActive = true,
                key = "feeds",
                type = "feeds",
                position = 1,
                title = "Update"
            ),
            FeedTabs.FeedData(
                isActive = true,
                key = "explore",
                type = "explore",
                position = 2,
                title = "Explore"
            )
        )
    )


    override suspend fun getWhitelist(): WhitelistQuery {
        return whitelistResponse
    }

    override suspend fun getDynamicTabs(): FeedTabs {
        return dynamicTabsResponse
    }

    override suspend fun clearDynamicTabCache() {

    }

    override fun getFeedContentForm(subscriber: Subscriber<GraphqlResponse>) {

    }

    fun setWhitelistResponse(response: WhitelistQuery) {
        this.whitelistResponse = response
    }

    fun setDynamicTabResponse(response: FeedTabs) {
        this.dynamicTabsResponse = response
    }
}
