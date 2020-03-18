package com.tokopedia.feedplus.view.subscriber

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.feedplus.data.pojo.*
import com.tokopedia.feedplus.view.listener.FeedPlusDetail
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderModel
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailItemModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber
import java.util.*

/**
 * @author by nisie on 5/24/17.
 */
class FeedDetailSubscriber(private val viewListener: FeedPlusDetail.View, private val page: Int) : Subscriber<GraphqlResponse>() {
    override fun onCompleted() {}
    override fun onError(e: Throwable) {
        viewListener.onErrorGetFeedDetail(
                ErrorHandler.getErrorMessage(viewListener.activity, e)
        )
    }

    override fun onNext(graphqlResponse: GraphqlResponse) {
        val feedQuery = graphqlResponse.getData<FeedQuery>(FeedQuery::class.java)
        if (page == 1) {
            viewListener.dismissLoading()
            if (!hasFeed(feedQuery)) {
                viewListener.onEmptyFeedDetail()
                return
            }
        } else {
            viewListener.dismissLoadingMore()
            if (!hasFeed(feedQuery)) {
                viewListener.setHasNextPage(false)
                return
            }
        }
        val feedList = feedQuery.feed.data
        val feedDetail = feedList[0]
        val headerViewModel = createHeaderViewModel(
                feedDetail.createTime,
                feedDetail.source.shop,
                feedDetail.content.statusActivity,
                feedDetail.id)
        viewListener.onSuccessGetFeedDetail(
                headerViewModel,
                convertToViewModel(feedDetail),
                checkHasNextPage(feedQuery))
    }

    private fun getRating(rating: Float): Double {
        return rating.toDouble() / MAX_RATING * NUM_STARS
    }

    private fun hasFeed(feedQuery: FeedQuery?): Boolean {
        return (feedQuery != null && feedQuery.feed != null && feedQuery.feed.data != null && feedQuery.feed.data.isNotEmpty()
                && feedQuery.feed.data[0] != null && feedQuery.feed.data[0].content != null && feedQuery.feed.data[0].content.products != null && feedQuery.feed.data[0].content.products.isNotEmpty())
    }

    private fun checkHasNextPage(feedQuery: FeedQuery): Boolean {
        return try {
            feedQuery.feed.data[0].meta.isHasNextPage
        } catch (e: NullPointerException) {
            false
        }
    }

    private fun convertToViewModel(feedDetail: Feed): ArrayList<Visitable<*>> {
        val listDetail = ArrayList<Visitable<*>>()
        if (feedDetail.content != null && feedDetail.content.products != null) {
            for (productFeed in feedDetail.content.products) {
                listDetail.add(createProductViewModel(productFeed))
            }
        }
        return listDetail
    }

    private fun createProductViewModel(productFeed: ProductFeedType): FeedDetailItemModel {
        return FeedDetailItemModel(
                productFeed.id,
                productFeed.name,
                productFeed.price,
                productFeed.priceOriginal,
                productFeed.image,
                productFeed.productLink,
                productFeed.cashback,
                productFeed.wishlist,
                productFeed.tags,
                getRating(productFeed.rating),
                productFeed.countReview
        )
    }

    private fun getIsWholesale(wholesale: List<Wholesale>): Boolean {
        return wholesale.isNotEmpty()
    }

    private fun createHeaderViewModel(create_time: String,
                                      shop: ShopDetail,
                                      status_activity: String,
                                      activityId: String): FeedDetailHeaderModel {
        return FeedDetailHeaderModel(shop.id,
                shop.name,
                shop.avatar,
                shop.isGold,
                create_time,
                shop.isOfficial,
                shop.shareLinkURL,
                shop.shareLinkDescription,
                status_activity,
                activityId
        )
    }

    companion object {
        private const val MAX_RATING = 100
        private const val NUM_STARS = 5
    }

}