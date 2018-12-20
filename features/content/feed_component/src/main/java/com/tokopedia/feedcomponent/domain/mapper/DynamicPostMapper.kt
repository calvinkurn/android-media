package com.tokopedia.feedcomponent.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.pojo.FeedQuery
import com.tokopedia.feedcomponent.data.pojo.feed.Feed
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by milhamj on 20/12/18.
 */
class DynamicPostMapper @Inject constructor(): Func1<GraphqlResponse, MutableList<Visitable<Any>>> {

    companion object {
        private const val TYPE_CARDRECOM = "cardrecom"
        private const val TYPE_CARDPOST = "cardpost"
        private const val TYPE_CARDBANNER = "cardbanner"
    }

    override fun call(t: GraphqlResponse?): MutableList<Visitable<Any>> {
        val feedQuery: FeedQuery? = t?.getData(FeedQuery::class.java)
        val posts: MutableList<Visitable<Any>> = ArrayList()
        feedQuery?.let {
            for (feed in it.feedv2.data) {
                when (feed.type) {
                    TYPE_CARDBANNER -> mapCardBanner(feed)
                    TYPE_CARDRECOM -> mapCardRecommendation(feed)
                    TYPE_CARDPOST -> mapCardPost(feed)
                }
            }
        }
        return posts
    }

    private fun mapCardBanner(feed: Feed) {

    }

    private fun mapCardRecommendation(feed: Feed) {
//        feed.content.cardRecommendation.
    }

    private fun mapCardPost(feed: Feed) {

    }
}