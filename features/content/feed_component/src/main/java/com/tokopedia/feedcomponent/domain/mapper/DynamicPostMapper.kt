package com.tokopedia.feedcomponent.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.pojo.FeedQuery
import com.tokopedia.feedcomponent.data.pojo.TemplateData
import com.tokopedia.feedcomponent.data.pojo.feed.Feed
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Body
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Media
import com.tokopedia.feedcomponent.data.pojo.template.Template
import com.tokopedia.feedcomponent.domain.model.DynamicFeedsDomainModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerItemViewModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.image.ImagePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollOptionViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.youtube.YoutubeViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.RecommendationCardViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by milhamj on 20/12/18.
 */
class DynamicPostMapper @Inject constructor() : Func1<GraphqlResponse, DynamicFeedsDomainModel> {

    companion object {
        private const val TYPE_CARDRECOM = "cardrecom"
        private const val TYPE_CARDPOST = "cardpost"
        private const val TYPE_CARDBANNER = "cardbanner"

        private const val POST_GRID = "productgrid"

        private const val CONTENT_IMAGE = "image"
        private const val CONTENT_YOUTUBE = "youtube"
        private const val CONTENT_VOTE = "vote"
    }

    @Suppress("UNCHECKED_CAST")
    override fun call(t: GraphqlResponse?): DynamicFeedsDomainModel {
        val feedQuery = t?.getData<FeedQuery?>(FeedQuery::class.java)
        val posts: MutableList<Visitable<*>> = ArrayList()
        var lastCursor: String = ""
        feedQuery?.let {
            for (feed in it.feedv2.data) {
                val templateData: TemplateData = it.feedv2.included.template.firstOrNull { templateData ->
                    templateData.id == feed.template
                } ?: break

                when (feed.type) {
                    TYPE_CARDBANNER -> posts.add(mapCardBanner(feed, templateData.template))
                    TYPE_CARDRECOM -> posts.add(mapCardRecommendation(feed, templateData.template))
                    TYPE_CARDPOST -> posts.add(mapCardPost(feed, templateData.template))
                }
            }

            lastCursor = it.feedv2.meta.lastCursor
        }
        return DynamicFeedsDomainModel(
                posts,
                lastCursor
        )
    }

    private fun mapCardBanner(feed: Feed, template: Template): BannerViewModel {
        val bannerList: MutableList<BannerItemViewModel> = ArrayList()
        for (media in feed.content.cardbanner.body.media) {
            val id = try {
                media.id.toInt()
            } catch (e: NumberFormatException) {
                0
            }

            bannerList.add(BannerItemViewModel(
                    id,
                    media.thumbnail,
                    media.appLink
            ))
        }

        return BannerViewModel(
                bannerList,
                feed.content.cardbanner.title,
                template
        )
    }

    private fun mapCardRecommendation(feed: Feed, template: Template): FeedRecommendationViewModel {
        val cards: MutableList<RecommendationCardViewModel> = ArrayList()
        for (card in feed.content.cardRecommendation.items) {
            val image1 = if (card.media.size > 0) card.media[0].thumbnail else ""
            val image2 = if (card.media.size > 1) card.media[1].thumbnail else ""
            val image3 = if (card.media.size > 2) card.media[2].thumbnail else ""

            cards.add(RecommendationCardViewModel(
                    image1,
                    image2,
                    image3,
                    card.header.avatar,
                    card.header.avatarBadgeImage,
                    card.header.avatarTitle,
                    card.header.avatarDescription,
                    card.header.followCta,
                    template.cardrecom.item
            ))
        }

        return FeedRecommendationViewModel(
                feed.content.cardRecommendation.title,
                cards,
                template
        )
    }

    private fun mapCardPost(feed: Feed, template: Template): DynamicPostViewModel {
        val contentList: MutableList<BasePostViewModel> = mapPostContent(feed.content.cardpost.body)

        return DynamicPostViewModel(
                feed.id,
                feed.content.cardpost.title,
                feed.content.cardpost.header,
                feed.content.cardpost.footer,
                feed.content.cardpost.body.caption,
                contentList,
                template
        )
    }

    private fun mapPostContent(body: Body): MutableList<BasePostViewModel> {
        val list: MutableList<BasePostViewModel> = ArrayList()

        //TODO milhamj delete this
        list.add(ImagePostViewModel("https://res.cloudinary.com/he2ebbhcc/image/upload/c_fill,f_auto,g_center,h_500,w_500/v1477492779/1158.jpg"))
        for (media in body.media) {
            when (media.type) {
                CONTENT_IMAGE -> list.add(mapPostImage(media))
                CONTENT_YOUTUBE -> list.add(mapPostYoutube(media))
                CONTENT_VOTE -> list.add(mapPostPoll(media))
            }
        }

        return list
    }

    private fun mapPostImage(media: Media): ImagePostViewModel {
        return ImagePostViewModel(
                media.thumbnail
        )
    }

    private fun mapPostYoutube(media: Media): YoutubeViewModel {
        return YoutubeViewModel(
                media.id
        )
    }

    private fun mapPostPoll(media: Media): PollViewModel {
        val options: MutableList<PollOptionViewModel> = ArrayList()

        for (item in media.mediaItems) {
            val percentageNumber = try {
                item.percentage.toInt()
            } catch (e: NumberFormatException) {
                0
            }

            options.add(PollOptionViewModel(
                    item.id,
                    item.text,
                    item.thumbnail,
                    item.applink,
                    item.percentage,
                    percentageNumber,
                    checkIfPollOptionSelected(media.isVoted, item.isSelected)
            ))
        }

        return PollViewModel(
                media.id,
                media.totalVoterFmt,
                media.isVoted,
                options
        )
    }

    private fun checkIfPollOptionSelected(isVoted: Boolean, isSelected: Boolean): Int {
        return if (isVoted && isSelected) {
            PollOptionViewModel.SELECTED
        } else if (isVoted) {
            PollOptionViewModel.UNSELECTED
        } else {
            PollOptionViewModel.DEFAULT
        }
    }
}