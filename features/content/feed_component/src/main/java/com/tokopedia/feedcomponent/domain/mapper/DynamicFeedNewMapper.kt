package com.tokopedia.feedcomponent.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCardDataItem
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXHome
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.*
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerItemViewModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.highlight.HighlightCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero

private const val TYPE_FEED_X_CARD_PLACEHOLDER: String = "FeedXCardPlaceholder"
private const val TYPE_FEED_X_CARD_BANNERS: String = "FeedXCardBanners"
private const val TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT: String = "FeedXCardProductsHighlight"
private const val TYPE_FEED_X_CARD_POST: String = "FeedXCardPost"
private const val TYPE_TOPADS_HEADLINE = "topads_headline"
private const val TYPE_CARD_PLAY_CAROUSEL = "play_carousel"

const val TYPE_IMAGE = "image"

object DynamicFeedNewMapper {

    fun map(feedXHome: FeedXHome): DynamicFeedDomainModel {
        val posts: MutableList<Visitable<*>> = ArrayList()
        var firstPageCursor = ""

        feedXHome.items.forEach {
            when (it.typename) {
                TYPE_FEED_X_CARD_PLACEHOLDER -> {
                    if (it.type == TYPE_TOPADS_HEADLINE) {
                        mapCardHeadline(posts)
                    } else if (it.type == TYPE_CARD_PLAY_CAROUSEL) {
                        mapCardCarousel(posts)
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
        //please remove this code if you want to show for video post
        val media = ArrayList<FeedXMedia>()
        feedXCard.media.map {
            if (it.type == TYPE_IMAGE) {
                media.add(it)
            }
        }
        feedXCard.media = media
        val dynamicPostUiModel = DynamicPostUiModel(feedXCard.copyPostData())
        posts.add(dynamicPostUiModel)
    }

    private fun mapCardHeadline(posts: MutableList<Visitable<*>>) {
        val topadsHeadlineUiModel = TopadsHeadlineUiModel(topadsHeadLinePage = TopAdsHeadlineActivityCounter.page++)
        posts.add(topadsHeadlineUiModel)
    }

    private fun mapCardCarousel(posts: MutableList<Visitable<*>>) {
        posts.add(CarouselPlayCardViewModel())
    }

    private fun mapCardBanner(posts: MutableList<Visitable<*>>, items: List<FeedXCardDataItem>) {
        val bannerList: MutableList<BannerItemViewModel> = ArrayList()
        items.forEach { media ->
            val id: Int = media.id.toIntOrNull() ?: 0
            bannerList.add(BannerItemViewModel(
                    id, media.coverUrl, media.appLink
            ))
        }
        if (bannerList.size > 0) {
            posts.add(BannerViewModel(bannerList)
            )
        }
    }

    private fun mapCardHighlight(posts: MutableList<Visitable<*>>, feed: FeedXCard) {
        if (feed.products.isNotEmpty()) {
            val dynamicPostUiModel = DynamicPostUiModel(feed.copyPostData())
            posts.add(dynamicPostUiModel)
        }
    }
}

private fun mapHighlightContent(cardHighlight: FeedXCard): MutableList<HighlightCardViewModel> {
    val list: MutableList<HighlightCardViewModel> = ArrayList()
    for (item in cardHighlight.products) {
        list.add(HighlightCardViewModel(
                item.id.toIntOrZero(),
                0,
                item.coverURL,
                item.appLink,
                "",
                header = getHeader(cardHighlight),
                footer = getFooter(cardHighlight)
        ))
    }
    return list
}

private fun getFooter(feedXCard: FeedXCard): Footer {
    return Footer(like = Like(fmt = feedXCard.like.label, value = feedXCard.like.count, isChecked = feedXCard.like.isLiked),
            comment = Comment(feedXCard.comments.label, feedXCard.comments.count),
            share = Share(description = feedXCard.share.label, url = feedXCard.share.operation))
    //need to confirm for share and button cta
}

private fun getHeader(feedXCard: FeedXCard): Header {
    return Header(avatar = feedXCard.author.logoURL, avatarApplink = feedXCard.author.appLink,
            avatarBadgeImage = feedXCard.author.badgeURL, avatarTitle = feedXCard.author.name,
            avatarDescription = feedXCard.author.description, avatarWeblink = feedXCard.author.webLink,
            followCta = FollowCta(authorID = feedXCard.author.id, isFollow = feedXCard.followers.isFollowed,
                    authorType = if (feedXCard.author.type == 1) {
                        FollowCta.AUTHOR_USER
                    } else {
                        FollowCta.AUTHOR_SHOP
                    }))
}

