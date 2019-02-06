package com.tokopedia.feedcomponent.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.pojo.FeedQuery
import com.tokopedia.feedcomponent.data.pojo.TemplateData
import com.tokopedia.feedcomponent.data.pojo.feed.Feed
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Body
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Media
import com.tokopedia.feedcomponent.data.pojo.template.Template
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerItemViewModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.TrackingBannerModel
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridItemViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.image.ImagePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentOptionViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.youtube.YoutubeViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.RecommendationCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.TrackingRecommendationModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by milhamj on 20/12/18.
 */
class DynamicFeedMapper @Inject constructor() : Func1<GraphqlResponse, DynamicFeedDomainModel> {

    companion object {
        private const val TYPE_CARDRECOM = "cardrecom"
        private const val TYPE_CARDPOST = "cardpost"
        private const val TYPE_CARDBANNER = "cardbanner"

        private const val CONTENT_IMAGE = "image"
        private const val CONTENT_YOUTUBE = "youtube"
        private const val CONTENT_VOTE = "vote"
        private const val CONTENT_GRID = "productgrid"

        private const val ACTIVITY_TOPADS = "topads"
    }

    @Suppress("UNCHECKED_CAST")
    override fun call(t: GraphqlResponse?): DynamicFeedDomainModel {
        val feedQuery = t?.getData<FeedQuery?>(FeedQuery::class.java)
        val posts: MutableList<Visitable<*>> = ArrayList()
        var lastCursor = ""
        var hasNext = false
        feedQuery?.let {
            for (feed in it.feedv2.data) {
                val templateData: TemplateData = it.feedv2.included.template.firstOrNull { templateData ->
                    templateData.id == feed.template
                } ?: break

                when (feed.type) {
                    TYPE_CARDBANNER -> mapCardBanner(posts, feed, templateData.template)
                    TYPE_CARDRECOM -> {
                        if (feed.activity != ACTIVITY_TOPADS) {
                            mapCardRecommendation(posts, feed, templateData.template)
                        } else {
                            mapTopadsShop(posts, feed, templateData.template)
                        }
                    }
                    TYPE_CARDPOST -> {
                        if (feed.activity != ACTIVITY_TOPADS) {
                            mapCardPost(posts, feed, templateData.template)
                        }
                    }
                }
            }

            lastCursor = it.feedv2.meta.lastCursor
            hasNext = it.feedv2.meta.hasNextPage && lastCursor.isNotEmpty()
        }

        return DynamicFeedDomainModel(
                posts,
                lastCursor,
                hasNext
        )
    }

    private fun mapCardBanner(posts: MutableList<Visitable<*>>, feed: Feed, template: Template) {
        val bannerList: MutableList<BannerItemViewModel> = ArrayList()

        feed.content.cardbanner.body.media.forEachIndexed { index, media ->
            val id: Int = media.id.toIntOrNull() ?: 0
            val trackBannerModel = TrackingBannerModel(
                    feed.type,
                    feed.activity,
                    feed.tracking.type,
                    media.type,
                    media.tags.firstOrNull()?.linkType ?: "",
                    media.thumbnail,
                    feed.id,
                    feed.content.cardbanner.body.media.size,
                    index
            )

            bannerList.add(BannerItemViewModel(
                    id,
                    media.thumbnail,
                    media.appLink,
                    trackBannerModel
            ))
        }

        if (bannerList.size > 0) {
            posts.add(
                    BannerViewModel(
                            bannerList,
                            feed.content.cardbanner.title,
                            template
                    )
            )
        }
    }

    private fun mapTopadsShop(posts: MutableList<Visitable<*>>, feed: Feed,
                              template: Template) {
        val trackingList = arrayListOf<TrackingRecommendationModel>()
        feed.content.cardRecommendation.items.forEachIndexed { index, card ->
            trackingList.add(TrackingRecommendationModel(
                    feed.type,
                    feed.activity,
                    feed.tracking.type,
                    card.media.firstOrNull()?.type ?: "",
                    card.header.avatarTitle,
                    card.header.followCta.authorType,
                    card.header.followCta.authorID.toIntOrNull() ?: 0,
                    index
            ))
        }

        val topadsShopList = feed.tracking.topads.filter {
            it.shop != null && it.shopClickUrl != null
        } as MutableList

        posts.add(
                TopadsShopViewModel(
                        feed.content.cardRecommendation.title,
                        topadsShopList,
                        template,
                        trackingList
                )
        )
    }

    private fun mapCardRecommendation(posts: MutableList<Visitable<*>>, feed: Feed,
                                      template: Template) {
        val cards: MutableList<RecommendationCardViewModel> = ArrayList()

        feed.content.cardRecommendation.items.forEachIndexed { index, card ->
            val image1 = if (card.media.size > 0) card.media[0].thumbnail else ""
            val image2 = if (card.media.size > 1) card.media[1].thumbnail else ""
            val image3 = if (card.media.size > 2) card.media[2].thumbnail else ""

            val trackingRecommendationModel = TrackingRecommendationModel(
                    feed.type,
                    feed.activity,
                    feed.tracking.type,
                    card.media.firstOrNull()?.type ?: "",
                    card.header.avatarTitle,
                    card.header.followCta.authorType,
                    card.header.followCta.authorID.toIntOrNull() ?: 0,
                    index
            )

            if (card.header.avatarTitle.isNotEmpty()) {
                cards.add(RecommendationCardViewModel(
                        image1,
                        image2,
                        image3,
                        card.header.avatar,
                        card.header.avatarBadgeImage,
                        card.header.avatarTitle,
                        card.header.avatarDescription,
                        card.header.avatarApplink,
                        card.header.followCta,
                        template.cardrecom.item,
                        trackingRecommendationModel
                ))
            }
        }

        if (cards.size > 0) {
            posts.add(
                    FeedRecommendationViewModel(
                            feed.content.cardRecommendation.title,
                            cards,
                            template
                    )
            )
        }
    }

    private fun mapCardPost(posts: MutableList<Visitable<*>>, feed: Feed, template: Template) {
        val contentList: MutableList<BasePostViewModel> = mapPostContent(feed.content.cardpost.body, template)
        val trackingPostModel = mapPostTracking(feed)

        if (shouldAddCardPost(feed, contentList)) {
            posts.add(
                    DynamicPostViewModel(
                            feed.id,
                            feed.content.cardpost.title,
                            feed.content.cardpost.header,
                            feed.content.cardpost.footer,
                            feed.content.cardpost.body.caption,
                            contentList,
                            template,
                            trackingPostModel
                    )
            )
        }
    }

    private fun shouldAddCardPost(feed: Feed, contentList: MutableList<BasePostViewModel>): Boolean {
        val isGridNotEmpty =
                if (contentList.firstOrNull() is GridPostViewModel)
                    (contentList.firstOrNull() as GridPostViewModel).itemList.size > 0
                else
                    true

        return feed.content.cardpost.header.avatarTitle.isNotEmpty() &&
                feed.content.cardpost.body.media.isNotEmpty() &&
                contentList.size > 0 &&
                isGridNotEmpty
    }

    private fun mapPostContent(body: Body, template: Template): MutableList<BasePostViewModel> {
        val list: MutableList<BasePostViewModel> = ArrayList()

        for (media in body.media) {
            when (media.type) {
                CONTENT_IMAGE -> list.add(mapPostImage(media))
                CONTENT_YOUTUBE -> list.add(mapPostYoutube(media))
                CONTENT_VOTE -> list.add(mapPostPoll(media))
                CONTENT_GRID -> list.add(mapPostGrid(media, template))
            }
        }

        return list
    }

    private fun mapPostTracking(feed: Feed): TrackingPostModel {
        val media = feed.content.cardpost.body.media.firstOrNull()
        val mediaType = media?.type ?: ""
        val tagsType = media?.tags?.firstOrNull()?.linkType ?: ""

        return TrackingPostModel(
                feed.type,
                feed.activity,
                feed.tracking.type,
                mediaType,
                tagsType,
                feed.content.cardpost.footer.buttonCta.appLink,
                feed.id,
                feed.content.cardpost.body.media.size
        )
    }


    private fun mapPostImage(media: Media): ImagePostViewModel {
        return ImagePostViewModel(
                media.thumbnail,
                media.appLink
        )
    }

    private fun mapPostYoutube(media: Media): YoutubeViewModel {
        return YoutubeViewModel(
                media.id
        )
    }

    private fun mapPostPoll(media: Media): PollContentViewModel {
        val options: MutableList<PollContentOptionViewModel> = ArrayList()

        for (item in media.mediaItems) {
            val percentageNumber = try {
                item.percentage.toInt()
            } catch (e: NumberFormatException) {
                0
            }

            options.add(PollContentOptionViewModel(
                    item.id,
                    item.text,
                    item.thumbnail,
                    item.applink,
                    percentageNumber,
                    checkIfPollOptionSelected(media.isVoted, item.isSelected)
            ))
        }

        return PollContentViewModel(
                media.id,
                media.text,
                media.totalVoter,
                media.isVoted,
                options
        )
    }

    private fun mapPostGrid(media: Media, template: Template): GridPostViewModel {
        val itemList: MutableList<GridItemViewModel> = ArrayList()

        for (item in media.mediaItems) {
            itemList.add(GridItemViewModel(
                    item.id,
                    item.text,
                    item.price,
                    item.applink,
                    item.thumbnail
            ))
        }

        return GridPostViewModel(
                itemList,
                media.text,
                media.appLink,
                media.totalItems,
                template.cardpost.body.mediaGridButton
        )
    }

    private fun checkIfPollOptionSelected(isVoted: Boolean, isSelected: Boolean): Int {
        return if (isVoted && isSelected) {
            PollContentOptionViewModel.SELECTED
        } else if (isVoted) {
            PollContentOptionViewModel.UNSELECTED
        } else {
            PollContentOptionViewModel.DEFAULT
        }
    }
}
