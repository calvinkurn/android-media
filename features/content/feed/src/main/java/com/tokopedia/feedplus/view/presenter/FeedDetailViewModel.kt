package com.tokopedia.feedplus.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.data.pojo.Feed
import com.tokopedia.feedplus.data.pojo.FeedQuery
import com.tokopedia.feedplus.data.pojo.ProductFeedType
import com.tokopedia.feedplus.data.pojo.ShopDetail
import com.tokopedia.feedplus.view.repository.FeedDetailRepository
import com.tokopedia.feedplus.view.subscriber.FeedDetailViewState
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderModel
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailItemModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import java.lang.Exception
import java.util.*
import javax.inject.Inject

private const val MAX_RATING = 100
private const val NUM_STARS = 5

class FeedDetailViewModel @Inject constructor(private var feedDetailRepository: FeedDetailRepository,
                                              private val userSession: UserSessionInterface) : ViewModel() {

    private var feedDetailLiveData: MutableLiveData<FeedDetailViewState> = MutableLiveData()
    private var pagingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getPagingLiveData(): LiveData<Boolean> {
        return pagingLiveData
    }

    fun getFeedDetailLiveData(): LiveData<FeedDetailViewState> {
        return feedDetailLiveData
    }

    fun getFeedDetail(detailId: String, page: Int) {
        viewModelScope.launchCatchError(block = {
            if (page == 1) {
                feedDetailLiveData.value = FeedDetailViewState.LoadingState(isLoading = true, loadingMore = false)
            } else {
                feedDetailLiveData.value = FeedDetailViewState.LoadingState(isLoading = true, loadingMore = true)
            }
            val feedQuery = feedDetailRepository.fetchFeedDetail(detailId, page, userSession.userId?.toIntOrNull()
                    ?: 0)
            handleDataForFeedDetail(feedQuery, page)
        }, onError =
        {
            it.printStackTrace()
            feedDetailLiveData.value = FeedDetailViewState.Error(it)
        })
    }

    private fun handleDataForFeedDetail(feedQuery: FeedQuery, page: Int) {
        if (page == 1) {
            feedDetailLiveData.value = FeedDetailViewState.LoadingState(false, loadingMore = false)
            if (!hasFeed(feedQuery)) {
                feedDetailLiveData.value = FeedDetailViewState.SuccessWithNoData
                return
            }
        } else {
            feedDetailLiveData.value = FeedDetailViewState.LoadingState(false, loadingMore = true)
            if (!hasFeed(feedQuery)) {
                pagingLiveData.value = false
                return
            }
        }

        val feedList = feedQuery.feed.data
        val feedDetail = feedList.firstOrNull()
        feedDetail?.let {
            val headerViewModel = createFeedDetailHeaderModel(
                    it.createTime,
                    it.source.shop,
                    it.content.statusActivity,
                    it.id)
            feedDetailLiveData.value = FeedDetailViewState.Success(headerViewModel,
                    convertToFeedDetailModel(it),
                    checkHasNextPage(feedQuery))
        }
    }

    private fun hasFeed(feedQuery: FeedQuery): Boolean {
        return (feedQuery.feed != null && feedQuery.feed.data != null && feedQuery.feed.data.isNotEmpty()
                && feedQuery.feed.data[0] != null && feedQuery.feed.data[0].content != null && feedQuery.feed.data[0].content.products != null && feedQuery.feed.data[0].content.products.isNotEmpty())
    }

    private fun checkHasNextPage(feedQuery: FeedQuery): Boolean {
        return try {
            feedQuery.feed.data[0].meta.isHasNextPage
        } catch (e: Exception) {
            false
        }
    }

    private fun convertToFeedDetailModel(feedDetail: Feed): ArrayList<Visitable<*>> {
        val listDetail = ArrayList<Visitable<*>>()
        if (feedDetail.content != null && feedDetail.content.products != null) {
            for (productFeed in feedDetail.content.products) {
                listDetail.add(createFeedDetailItemModel(productFeed))
            }
        }
        return listDetail
    }

    private fun createFeedDetailItemModel(productFeed: ProductFeedType): FeedDetailItemModel {
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

    private fun createFeedDetailHeaderModel(create_time: String,
                                            shop: ShopDetail,
                                            status_activity: String,
                                            activityId: String): FeedDetailHeaderModel {
        return FeedDetailHeaderModel(shop.id,
                shop.name,
                shop.avatar,
                shop.isGold,
                shop.badgeUrl,
                create_time,
                shop.isOfficial,
                shop.shareLinkURL,
                shop.shareLinkDescription,
                status_activity,
                activityId
        )
    }

    private fun getRating(rating: Float): Double {
        return rating.toDouble() / MAX_RATING * NUM_STARS
    }

}