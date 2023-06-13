package com.tokopedia.feedcomponent.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.pojo.FeedQuery
import com.tokopedia.feedcomponent.data.pojo.TemplateData
import com.tokopedia.feedcomponent.data.pojo.feed.Cardpost
import com.tokopedia.feedcomponent.data.pojo.feed.Feed
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Media
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTag
import com.tokopedia.feedcomponent.data.pojo.template.Template
import com.tokopedia.feedcomponent.data.pojo.track.Tracking
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerItemModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.TopAdsBannerModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.TrackingBannerModel
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardModel
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridItemModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.MultimediaGridModel
import com.tokopedia.feedcomponent.view.viewmodel.post.image.ImagePostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentOptionModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentModel
import com.tokopedia.feedcomponent.view.viewmodel.post.video.VideoModel
import com.tokopedia.feedcomponent.view.viewmodel.post.youtube.YoutubeModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.RecommendationCardModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.TrackingRecommendationModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadLineV2Model
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopUiModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.util.throwIfNull
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by milhamj on 20/12/18.
 */

private const val TYPE_CARDRECOM = "cardrecom"
private const val TYPE_CARDPOST = "cardpost"
private const val TYPE_CARDBANNER = "cardbanner"
private const val TYPE_CARDHIGHLIGHT = "cardhighlight"
private const val TYPE_CARDPLAYCAROUSEL = "cardplaycarousel"
private const val TYPE_CARD_HEADLINE = "cardheadline"

private const val CONTENT_IMAGE = "image"
private const val CONTENT_YOUTUBE = "youtube"
private const val CONTENT_VIDEO = "video"
private const val CONTENT_VOTE = "vote"
private const val CONTENT_GRID = "productgrid"
private const val CONTENT_MULTIMEDIA = "mediagrid"

private const val ACTIVITY_TOPADS = "topads"
private const val ACTIVITY_TOPADS_BANNER = "topads_banner"
private const val ACTIVITY_TOPADS_HEADLINE = "topads_headline"
private const val AUTHOR_TOPADS_SHOP = "topads shop"

class DynamicFeedMapper @Inject constructor() : Func1<GraphqlResponse, DynamicFeedDomainModel> {

    var feedType: String = ""

    @Suppress("UNCHECKED_CAST")
    override fun call(t: GraphqlResponse?): DynamicFeedDomainModel {
        val feedQuery = t?.getData<FeedQuery?>(FeedQuery::class.java)
        val posts: MutableList<Visitable<*>> = ArrayList()
        var lastCursor = ""
        var firstPageCursor = ""
        var hasNext = false

        throwIfNull(feedQuery, DynamicFeedMapper::class.java)

        feedQuery?.let {
            for (feed in it.feedv2.data) {
                val templateData: TemplateData = it.feedv2.included.template.firstOrNull { templateData ->
                    templateData.id == feed.template.toString()
                } ?: break

                when (feed.type) {
                    TYPE_CARDBANNER -> {
                        if (feed.activity == ACTIVITY_TOPADS_BANNER) {
                            mapTopAdsBannerData(posts, feed, templateData.template)
                        } else {
                            mapCardBanner(posts, feed, templateData.template)
                        }
                    }
                    TYPE_CARDRECOM -> {
                        if (feed.activity != ACTIVITY_TOPADS) {
                            mapCardRecommendation(posts, feed, templateData.template)
                        } else {
                            mapTopAdsShop(posts, feed, templateData.template)
                        }
                    }
                    TYPE_CARDPOST -> {
                        if (feed.activity != ACTIVITY_TOPADS) {
                            mapCardPost(posts, feed, templateData.template)
                        }
                    }
                    TYPE_CARDPLAYCAROUSEL -> {
                        if (feed.activity != ACTIVITY_TOPADS) {
                            mapCardCarousel(posts)
                        }
                    }
                    TYPE_CARD_HEADLINE -> {
                        if(feed.activity == ACTIVITY_TOPADS_HEADLINE) {
                            mapCardHeadline(posts, templateData.template)
                        }
                    }
                }
            }

            lastCursor = it.feedv2.meta.lastCursor
            firstPageCursor = it.feedv2.meta.firstPageCursor
            hasNext = it.feedv2.meta.hasNextPage && lastCursor.isNotEmpty()
        }

        return DynamicFeedDomainModel(
                posts,
                lastCursor,
                firstPageCursor,
                hasNext
        )
    }

    private fun mapCardHeadline(posts: MutableList<Visitable<*>>, template: Template) {
        val topadsHeadlineUiModel = TopadsHeadlineUiModel(template = template, topadsHeadLinePage = TopAdsHeadlineActivityCounter.page++)
        val topadsHeadlineUiModelv2 = TopadsHeadLineV2Model(template = template, topadsHeadLinePage = TopAdsHeadlineActivityCounter.page++)

        posts.add(topadsHeadlineUiModel)
        posts.add(topadsHeadlineUiModelv2)
    }

    private fun mapTopAdsBannerData(posts: MutableList<Visitable<*>>, feed: Feed, template: Template) {
        val topAdsBannerModel = TopAdsBannerModel()
        topAdsBannerModel.title = feed.content.cardbanner.title
        topAdsBannerModel.template = template
        posts.add(topAdsBannerModel)
    }

    private fun mapCardBanner(posts: MutableList<Visitable<*>>, feed: Feed, template: Template) {
        val bannerList: MutableList<BannerItemModel> = mutableListOf()

        feed.content.cardbanner.body.media.forEachIndexed { index, media ->
            val id = media.id
            val trackBannerModel = TrackingBannerModel(
                    feed.type,
                    feed.activity,
                    feed.tracking.type,
                    media.type,
                    media.tags.firstOrNull()?.linkType ?: "",
                    media.thumbnail,
                    media.appLink,
                    feed.id,
                    feed.content.cardbanner.body.media.size,
                    index
            )

            bannerList.add(BannerItemModel(
                    id,
                    media.thumbnail,
                    media.appLink,
                    trackBannerModel,
                    mapTrackingData(media.tracking)
            ))
        }
    }

    private fun mapTopAdsShop(posts: MutableList<Visitable<*>>, feed: Feed,
                              template: Template) {
        val trackingRecommendationList = arrayListOf<TrackingRecommendationModel>()
        val trackingList = arrayListOf<TrackingModel>()
        if (feed.content.cardRecommendation.items.isNotEmpty()) {

            val topAdsShopList = feed.tracking.topads.filter {
                it.shop != null && it.shopClickUrl != null
            } as MutableList

            feed.content.cardRecommendation.items.forEachIndexed { index, card ->
                trackingRecommendationList.add(TrackingRecommendationModel(
                        feed.type,
                        feed.activity,
                        feed.tracking.type,
                        card.media.firstOrNull()?.type ?: "",
                        card.header.avatarTitle,
                        AUTHOR_TOPADS_SHOP,
                        card.header.followCta.authorID ,
                        index,
                        feed.tracking.topads.getOrNull(index)?.id?:""
                ))
                card.tracking.firstOrNull()?.let { tracking ->
                    trackingList.add(TrackingModel(
                            tracking.clickURL,
                            tracking.viewURL,
                            tracking.type,
                            tracking.source,
                            tracking.viewType,
                            tracking.recomID
                    ))
                }
                topAdsShopList[index].isFavorit = card.header.followCta.isFollow
            }

            posts.add(
                    TopadsShopUiModel(
                            feed.content.cardRecommendation.title,
                            topAdsShopList,
                            template,
                            trackingRecommendationList,
                            trackingList
                    )
            )
        }
    }

    private fun mapCardRecommendation(posts: MutableList<Visitable<*>>, feed: Feed,
                                      template: Template) {
        val cards: MutableList<RecommendationCardModel> = ArrayList()

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
                    card.header.followCta.authorID,
                    index
            )

            if (card.header.avatarTitle.isNotEmpty()) {
                cards.add(RecommendationCardModel(
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
                        trackingRecommendationModel,
                        mapTrackingData(card.tracking)
                ))
            }
        }
    }

    private fun mapCardPost(posts: MutableList<Visitable<*>>, feed: Feed, template: Template) {
        val contentList: MutableList<BasePostModel> = mapPostContent(feed.content.cardpost, template)
        val trackingPostModel = mapPostTracking(feed)

        val postTag = feed.content.cardpost.body.postTag.firstOrNull() ?: PostTag()
        if (shouldAddCardPost(feed, contentList, postTag)) {
            posts.add(
                    DynamicPostModel(
                            feed.id,
                            feed.content.cardpost.title,
                            feed.content.cardpost.header,
                            postTag,
                            feed.content.cardpost.footer,
                            feed.content.cardpost.body.caption,
                            contentList,
                            template,
                            trackingPostModel,
                            mapTrackingData(feed.content.cardpost.tracking),
                            feedType,
                            feed.activity
                    )
            )
        }
    }

    private fun shouldAddCardPost(feed: Feed, contentList: MutableList<BasePostModel>, postTag: PostTag): Boolean {
        val isGridNotEmpty =
                if (contentList.firstOrNull() is GridPostModel)
                    (contentList.firstOrNull() as GridPostModel).itemList.size > 0
                else
                    true

        return feed.content.cardpost.header.avatarTitle.isNotEmpty() &&
                ((feed.content.cardpost.body.media.isNotEmpty() && contentList.size > 0) || postTag.items.isNotEmpty()) &&
                isGridNotEmpty
    }

    private fun mapPostContent(cardPost: Cardpost, template: Template): MutableList<BasePostModel> {
        val list: MutableList<BasePostModel> = ArrayList()
        for (media in cardPost.body.media) {
            when (media.type) {
                CONTENT_IMAGE -> list.add(mapPostImage(media))
                CONTENT_YOUTUBE -> list.add(mapPostYoutube(media))
                CONTENT_VOTE -> list.add(mapPostPoll(media))
                CONTENT_GRID -> list.add(mapPostGrid(media, template))
                CONTENT_VIDEO -> {
                    if (media.thumbnail.isNotBlank()
                            && media.videoList.isNotEmpty()
                            && media.videoList[0].url.isNotBlank()) {
                        list.add(mapPostVideo(media))
                    }
                }
                CONTENT_MULTIMEDIA -> list.add(mapPostMultimedia(media, template))
            }
        }

        return list
    }

    private fun mapPostTracking(feed: Feed): TrackingPostModel {
        val media = feed.content.cardpost.body.media.firstOrNull()
        val mediaType = media?.type ?: ""
        val mediaUrl = media?.thumbnail ?: ""
        val tagsType = media?.tags?.firstOrNull()?.linkType ?: ""
        val authorId = feed.content.cardpost.header.followCta.authorID
        val recomId = feed.content.cardpost.tracking[0].recomID

        return TrackingPostModel(
                feed.type,
                feed.activity,
                feed.tracking.type,
                mediaType,
                mediaUrl,
                tagsType,
                feed.content.cardpost.footer.buttonCta.appLink,
                authorId,
                feed.id,
                feed.content.cardpost.body.media.size,
                recomId
        )
    }


    private fun mapPostImage(media: Media): ImagePostModel {
        return ImagePostModel(
                media.thumbnail,
                media.appLink,
                mapTrackingData(media.tracking)
        )
    }

    private fun mapPostYoutube(media: Media): YoutubeModel {
        return YoutubeModel(
                media.id,
                mapTrackingData(media.tracking)
        )
    }

    private fun mapPostVideo(media: Media): VideoModel {
        return VideoModel(
                media.id,
                media.thumbnail,
                media.videoList.getOrNull(0)?.url ?: "",
                redirectLink = media.appLink
        )
    }

    private fun mapPostMultimedia(media: Media, template: Template): MultimediaGridModel {
        val itemList: MutableList<GridItemModel> = ArrayList()

        for (item in media.mediaItems) {
            item.isSelected = true
            itemList.add(GridItemModel(
                    item.id,
                    item.text,
                    item.price,
                    item.priceOriginal,
                    item.applink,
                    item.thumbnail,
                    item.tags.toMutableList(),
                    mapTrackingData(item.tracking)
            ))
        }
        return MultimediaGridModel(
                itemList,
                media.mediaItems,
                media.text,
                media.appLink,
                media.totalItems,
                template.cardpost.body.mediaGridButton,
                mapTrackingData(media.tracking)
        )
    }

    private fun mapPostPoll(media: Media): PollContentModel {
        val options: MutableList<PollContentOptionModel> = ArrayList()

        for (item in media.mediaItems) {
            val percentageNumber = try {
                item.percentage.toIntOrZero()
            } catch (e: NumberFormatException) {
                0
            }

            options.add(PollContentOptionModel(
                    item.id,
                    item.text,
                    item.thumbnail,
                    item.applink,
                    percentageNumber,
                    checkIfPollOptionSelected(media.isVoted, item.isSelected)
            ))
        }

        return PollContentModel(
                media.id,
                media.text,
                media.totalVoter,
                media.isVoted,
                options,
                mapTrackingData(media.tracking)
        )
    }

    private fun mapPostGrid(media: Media, template: Template): GridPostModel {
        val itemList: MutableList<GridItemModel> = ArrayList()

        for (item in media.mediaItems) {
            itemList.add(GridItemModel(
                    item.id,
                    item.text,
                    item.price,
                    item.priceOriginal,
                    item.applink,
                    item.thumbnail,
                    item.tags.toMutableList(),
                    mapTrackingData(item.tracking)
            ))
        }

        return GridPostModel(
                itemList,
                media.text,
                media.appLink,
                media.totalItems,
                template.cardpost.body.mediaGridButton,
                mapTrackingData(media.tracking)
        )
    }

    private fun checkIfPollOptionSelected(isVoted: Boolean, isSelected: Boolean): Int {
        return if (isVoted && isSelected) {
            PollContentOptionModel.SELECTED
        } else if (isVoted) {
            PollContentOptionModel.UNSELECTED
        } else {
            PollContentOptionModel.DEFAULT
        }
    }

    private fun mapTrackingData(trackList: List<Tracking>): MutableList<TrackingModel> {
        val trackingList: MutableList<TrackingModel> = ArrayList()

        for (track in trackList) {
            trackingList.add(TrackingModel(
                    track.clickURL,
                    track.viewURL,
                    track.type,
                    track.source
            ))
        }
        return trackingList
    }

    private fun mapCardCarousel(posts: MutableList<Visitable<*>>) {
        posts.add(CarouselPlayCardModel())
    }
}

object TopAdsHeadlineActivityCounter {
    var page: Int = 1
}
