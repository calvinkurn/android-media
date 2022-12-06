package com.tokopedia.feedcomponent.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCardDataItem
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXHome
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerItemModel
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardModel
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomWidgetModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadLineV2Model
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero

private const val TYPE_FEED_X_CARD_PLACEHOLDER: String = "FeedXCardPlaceholder"
private const val TYPE_FEED_X_CARD_BANNERS: String = "FeedXCardBanners"
const val TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT: String = "FeedXCardProductsHighlight"
const val TYPE_FEED_X_CARD_POST: String = "FeedXCardPost"
const val TYPE_FEED_X_CARD_PLAY: String = "FeedXCardPlay"
private const val TYPE_TOPADS_HEADLINE = "topads_headline"
const val TYPE_TOPADS_HEADLINE_NEW = "topads_headline_new"
private const val TYPE_CARD_PLAY_CAROUSEL = "play_carousel"
private const val TYPE_CARD_SHOP_RECOMMENDATION = "shop_recommendation"
const val TYPE_LONG_VIDEO: String = "long-video"
const val TYPE_VIDEO: String = "video"
const val TYPE_IMAGE = "image"

object DynamicFeedNewMapper {

    fun map(feedXHome: FeedXHome, cursor: String, shouldShowNewTopadsOnly:Boolean): DynamicFeedDomainModel {
        val posts: MutableList<Visitable<*>> = ArrayList()
        var firstPageCursor = ""

        feedXHome.items.forEach {
            when (it.typename) {
                TYPE_FEED_X_CARD_PLACEHOLDER -> {
                    if (it.type == TYPE_TOPADS_HEADLINE) {
                        mapCardHeadline(posts, shouldShowNewTopadsOnly)
                    } else if (it.type == TYPE_CARD_PLAY_CAROUSEL && cursor.isEmpty()) {
                        mapCardCarousel(posts)
                    } else if (it.type == TYPE_CARD_SHOP_RECOMMENDATION) {
                        mapShopRecommendation(posts)
                    }
                }
                TYPE_FEED_X_CARD_BANNERS -> {
                    mapCardBanner(posts, it.items)
                }
                TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT -> {
                    mapCardHighlight(posts, it)
                }
                TYPE_FEED_X_CARD_POST -> {
                    mapCardPost(posts, it)
                }
                TYPE_FEED_X_CARD_PLAY -> {
                    mapCardVOD(posts, it)
                }
            }
        }
        val lastCursor: String = feedXHome.pagination.cursor
        if (firstPageCursor.isEmpty()) {
            firstPageCursor = feedXHome.pagination.cursor
        }
        val hasNext: Boolean = feedXHome.pagination.hasNext && lastCursor.isNotEmpty()

        return DynamicFeedDomainModel(posts, lastCursor, firstPageCursor, hasNext)
    }

    private fun mapCardPost(posts: MutableList<Visitable<*>>, feedXCard: FeedXCard) {
        val dynamicPostUiModel = DynamicPostUiModel(feedXCard.copyPostData(), mapPostTracking(feedXCard))
        posts.add(dynamicPostUiModel)
    }
    private fun mapCardVOD(posts: MutableList<Visitable<*>>, feedXCard: FeedXCard) {
        val dynamicPostUiModel = DynamicPostUiModel(feedXCard.copyPostData())
        posts.add(dynamicPostUiModel)
    }


    private fun mapCardHeadline(posts: MutableList<Visitable<*>>, shouldShowNewTopadsOnly: Boolean) {
        val headLinePge = TopAdsHeadlineActivityCounter.page++
        val topadsHeadlineUiModel = TopadsHeadlineUiModel(topadsHeadLinePage = headLinePge)
        val topadsHeadlineUiModelV2 = TopadsHeadLineV2Model(topadsHeadLinePage = headLinePge)
        if (!shouldShowNewTopadsOnly)
            posts.add(topadsHeadlineUiModel)
            posts.add(topadsHeadlineUiModelV2)

    }

    private fun mapCardCarousel(posts: MutableList<Visitable<*>>) {
        posts.add(CarouselPlayCardModel())
    }

    private fun mapShopRecommendation(posts: MutableList<Visitable<*>>) {
        posts.add(ShopRecomWidgetModel())
    }

    private fun mapCardBanner(posts: MutableList<Visitable<*>>, items: List<FeedXCardDataItem>) {
        val bannerList: MutableList<BannerItemModel> = ArrayList()
        items.forEach { media ->
            val id = media.id
            bannerList.add(BannerItemModel(
                    id, media.coverUrl, media.appLink
            ))
        }
    }

    private fun mapCardHighlight(posts: MutableList<Visitable<*>>, feed: FeedXCard) {
        if (feed.products.isNotEmpty()) {
            val dynamicPostUiModel = DynamicPostUiModel(feed.copyPostData())
            posts.add(dynamicPostUiModel)
        }
    }
    private fun mapPostTracking(feed: FeedXCard): TrackingPostModel {
        val media = feed.media.firstOrNull()
        val mediaType = media?.type ?: ""
        val mediaUrl = media?.mediaUrl ?: ""
        val tagsType = ""
        val authorId = feed.author.id
        val recomId = feed.id.toLongOrZero()

        return TrackingPostModel(
                feed.type,
                feed.typename,
                "",
                mediaType,
                mediaUrl,
                tagsType,
                feed.appLink,
                authorId,
                feed.id,
                feed.media.size,
                recomId
        )
    }

}


