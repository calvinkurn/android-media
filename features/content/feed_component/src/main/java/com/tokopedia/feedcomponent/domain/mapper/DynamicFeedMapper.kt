package com.tokopedia.feedcomponent.domain.mapper

import com.crashlytics.android.Crashlytics
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.BuildConfig
import com.tokopedia.feedcomponent.data.pojo.FeedQuery
import com.tokopedia.feedcomponent.data.pojo.TemplateData
import com.tokopedia.feedcomponent.data.pojo.feed.Cardpost
import com.tokopedia.feedcomponent.data.pojo.feed.Feed
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Media
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.MediaItem
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTag
import com.tokopedia.feedcomponent.data.pojo.template.Template
import com.tokopedia.feedcomponent.data.pojo.track.Tracking
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerItemViewModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.TrackingBannerModel
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridItemViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.MultimediaGridViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.image.ImagePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentOptionViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.video.VideoViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.youtube.YoutubeViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.RecommendationCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.TrackingRecommendationModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.util.ContainNullException
import com.tokopedia.kotlin.util.isContainNull
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by milhamj on 20/12/18.
 */
class DynamicFeedMapper @Inject constructor() : Func1<GraphqlResponse, DynamicFeedDomainModel> {

    var count = 1;
    companion object {
        private const val TYPE_CARDRECOM = "cardrecom"
        private const val TYPE_CARDPOST = "cardpost"
        private const val TYPE_CARDBANNER = "cardbanner"

        private const val CONTENT_IMAGE = "image"
        private const val CONTENT_YOUTUBE = "youtube"
        private const val CONTENT_VIDEO = "video"
        private const val CONTENT_VOTE = "vote"
        private const val CONTENT_GRID = "productgrid"
        private const val CONTENT_MULTIMEDIA = "mediagrid"

        private const val ACTIVITY_TOPADS = "topads"

        private const val AUTHOR_TOPADS_SHOP = "topads shop"
    }

    var feedType: String = ""

    @Suppress("UNCHECKED_CAST")
    override fun call(t: GraphqlResponse?): DynamicFeedDomainModel {
        val feedQuery = t?.getData<FeedQuery?>(FeedQuery::class.java)
        val posts: MutableList<Visitable<*>> = ArrayList()
        var lastCursor = ""
        var hasNext = false

        isContainNull(feedQuery) {
            val exception = ContainNullException("Found $it in ${DynamicFeedMapper::class.java.simpleName}")
            if (!BuildConfig.DEBUG) {
                Crashlytics.logException(exception)
            }
            throw exception
        }

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
                    media.appLink,
                    feed.id,
                    feed.content.cardbanner.body.media.size,
                    index
            )

            bannerList.add(BannerItemViewModel(
                    id,
                    media.thumbnail,
                    media.appLink,
                    trackBannerModel,
                    mapTrackingData(media.tracking)
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
        if (feed.content.cardRecommendation.items.isNotEmpty()) {
            feed.content.cardRecommendation.items.forEachIndexed { index, card ->
                trackingList.add(TrackingRecommendationModel(
                        feed.type,
                        feed.activity,
                        feed.tracking.type,
                        card.media.firstOrNull()?.type ?: "",
                        card.header.avatarTitle,
                        AUTHOR_TOPADS_SHOP,
                        card.header.followCta.authorID.toIntOrNull() ?: 0,
                        index,
                        feed.tracking.topads.getOrNull(index)?.id.toIntOrZero()
                ))
            }

            val topadsShopList = feed.tracking.topads.filter {
                it.shop != null && it.shopClickUrl != null
            } as MutableList

            feed.content.cardRecommendation.items.forEachIndexed { index, item ->
                topadsShopList.get(index).isFavorit = item.header.followCta.isFollow
            }

            posts.add(
                    TopadsShopViewModel(
                            feed.content.cardRecommendation.title,
                            topadsShopList,
                            template,
                            trackingList
                    )
            )
        }
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
                        trackingRecommendationModel,
                        mapTrackingData(card.tracking)
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
        val contentList: MutableList<BasePostViewModel> = mapPostContent(feed.content.cardpost, template)
        val trackingPostModel = mapPostTracking(feed)

        if (shouldAddCardPost(feed, contentList)) {
            val postTag = feed.content.cardpost.body.postTag.firstOrNull() ?: PostTag()
            posts.add(
                    DynamicPostViewModel(
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
                            feedType
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

    private fun mapPostContent(cardPost: Cardpost, template: Template): MutableList<BasePostViewModel> {
        val list: MutableList<BasePostViewModel> = ArrayList()
        if (cardPost.body.media.isNotEmpty()) {
            cardPost.body.media = getDataMultimedia(cardPost.body.media, count)
            if (count < 6) {
                count++
            }
        }
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
                feed.content.cardpost.body.media.size
        )
    }


    private fun mapPostImage(media: Media): ImagePostViewModel {
        return ImagePostViewModel(
                media.thumbnail,
                media.appLink,
                mapTrackingData(media.tracking)
        )
    }

    private fun mapPostYoutube(media: Media): YoutubeViewModel {
        return YoutubeViewModel(
                media.id,
                mapTrackingData(media.tracking)
        )
    }

    private fun mapPostVideo(media: Media): VideoViewModel {
        return VideoViewModel(
                media.id,
                media.thumbnail,
                media.videoList.getOrNull(0)?.url ?: ""
        )
    }

    private fun mapPostMultimedia(media: Media, template: Template): MultimediaGridViewModel {
        val itemList: MutableList<GridItemViewModel> = ArrayList()

        for (item in media.mediaItems) {
            itemList.add(GridItemViewModel(
                    item.id,
                    item.text,
                    item.price,
                    item.applink,
                    item.thumbnail,
                    mapTrackingData(item.tracking)
            ))
        }
        return MultimediaGridViewModel(
                itemList,
                media.mediaItems,
                media.text,
                media.appLink,
                media.totalItems,
                template.cardpost.body.mediaGridButton,
                mapTrackingData(media.tracking)
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
                options,
                mapTrackingData(media.tracking)
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
                    item.thumbnail,
                    mapTrackingData(item.tracking)
            ))
        }

        return GridPostViewModel(
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
            PollContentOptionViewModel.SELECTED
        } else if (isVoted) {
            PollContentOptionViewModel.UNSELECTED
        } else {
            PollContentOptionViewModel.DEFAULT
        }
    }

    private fun mapTrackingData(trackList: List<Tracking>): MutableList<TrackingViewModel> {
        val trackingList: MutableList<TrackingViewModel> = ArrayList()

        for (track in trackList) {
            trackingList.add(TrackingViewModel(
                    track.clickURL,
                    track.viewURL,
                    track.type,
                    track.source
            ))
        }
        return trackingList
    }

    private fun getDataMultimedia(mediaList : List<Media>, count: Int): List<Media> {
        var newMediaList: ArrayList<Media> = ArrayList()
        for (media: Media in mediaList) {
            val newMediaItemList: ArrayList<MediaItem> = ArrayList()
            media.type = CONTENT_MULTIMEDIA
            for (i in 1..count) {
               newMediaItemList.add(mappingMultimediaMediaItem())
            }
            media.mediaItems = newMediaItemList
            newMediaList.add(media)
        }
        return newMediaList
    }

    private fun mappingMultimediaMediaItem(): MediaItem {
        var mediaItem = MediaItem()
        mediaItem.thumbnail = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/4/19/1592907/1592907_e1fde789-3c9e-4478-9f6d-aaed7ab0a1aa_2000_2000.jpg"
        return mediaItem
    }
}