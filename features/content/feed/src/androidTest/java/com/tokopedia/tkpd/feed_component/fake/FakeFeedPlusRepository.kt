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

    override suspend fun getWhitelist(): WhitelistQuery {
        return WhitelistQuery(
            whitelist = Whitelist(
                authors = listOf(
                    Author(
                        type = "content-user",
                        thumbnail = "https://images.tokopedia.net/img/cache/100-square/default_picture_user/default_toped-24.jpg"
                    )
                )
            )
        )
    }

    override suspend fun getDynamicTabs(): FeedTabs {

    }

    override suspend fun clearDynamicTabCache() {

    }

    override fun getFeedContentForm(subscriber: Subscriber<GraphqlResponse>) {

    }
}
