package com.tokopedia.feedcomponent.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.pojo.FeedQuery
import com.tokopedia.feedcomponent.data.pojo.TemplateData
import com.tokopedia.feedcomponent.data.pojo.feed.Feed
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Body
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Media
import com.tokopedia.feedcomponent.data.pojo.template.Template
import com.tokopedia.feedcomponent.view.viewmodel.CardTitle
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.image.ImagePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.RecommendationCardViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by milhamj on 20/12/18.
 */
class DynamicPostMapper @Inject constructor() : Func1<GraphqlResponse, MutableList<Visitable<*>>> {

    companion object {
        private const val TYPE_CARDRECOM = "cardrecom"
        private const val TYPE_CARDPOST = "cardpost"
        private const val TYPE_CARDBANNER = "cardbanner"

        private const val POST_GRID = "productgrid"

        private const val CONTENT_IMAGE = "image"
    }

    @Suppress("UNCHECKED_CAST")
    override fun call(t: GraphqlResponse?): MutableList<Visitable<*>> {
        val feedQuery = t?.getData<FeedQuery?>(FeedQuery::class.java)
        val posts: MutableList<Visitable<*>> = ArrayList()
        feedQuery?.let {
            for (feed in it.feedv2.data) {
                val templateData: TemplateData = it.feedv2.included.template.firstOrNull { templateData ->
                    templateData.id == feed.template
                } ?: break

                when (feed.type) {
                    //TODO milhamj
//                    TYPE_CARDBANNER -> posts.add(mapCardBanner(feed))
                    TYPE_CARDRECOM -> posts.add(mapCardRecommendation(feed, templateData.template))
                    TYPE_CARDPOST -> posts.add(mapCardPost(feed, templateData.template))
                }
            }
        }
        return posts
    }

//    private fun mapCardBanner(feed: Feed) {
//
//    }

    private fun mapCardRecommendation(feed: Feed, template: Template): FeedRecommendationViewModel {
        val cardTitle = CardTitle(
                feed.content.cardRecommendation.title.text,
                feed.content.cardRecommendation.title.action.action,
                feed.content.cardRecommendation.title.action.appLink
        )
        val cards: MutableList<RecommendationCardViewModel> = ArrayList()

        for (card in feed.content.cardRecommendation.items) {
            cards.add(RecommendationCardViewModel(
                    "",
                    "",
                    "",
                    card.header.avatar,
                    card.header.avatarBadgeImage,
                    card.header.avatarTitle,
                    card.header.avatarDescription,
                    card.header.followCta.textTrue
            ))
        }

        return FeedRecommendationViewModel(
                cardTitle,
                cards
        )
    }

    private fun mapCardPost(feed: Feed, template: Template): DynamicPostViewModel {
        val header = feed.content.cardpost.header
        val footer = feed.content.cardpost.footer
        val contentList: MutableList<BasePostViewModel> = mapPostContent(feed.content.cardpost.body)

        return DynamicPostViewModel(
                feed.content.cardpost.title,
                header,
                contentList = contentList,
                template = template
        )
    }

    private fun mapPostContent(body: Body): MutableList<BasePostViewModel> {
        val list = ArrayList<BasePostViewModel>()
        for (media in body.media) {
            when (media.type) {
                CONTENT_IMAGE -> mapPostImage(media)
            }
        }
        return list
    }

    private fun mapPostImage(media: Media): BasePostViewModel {
        return ImagePostViewModel(
                media.thumbnail
        )
    }
}