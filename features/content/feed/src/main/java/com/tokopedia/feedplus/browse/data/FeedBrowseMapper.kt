package com.tokopedia.feedplus.browse.data

import com.tokopedia.content.common.model.Content
import com.tokopedia.content.common.model.ContentItem
import com.tokopedia.content.common.model.ContentSlotMeta
import com.tokopedia.content.common.model.FeedXHeaderResponse
import com.tokopedia.content.common.model.WidgetSlot
import com.tokopedia.feedplus.browse.data.model.AuthorWidgetModel
import com.tokopedia.feedplus.browse.data.model.BannerWidgetModel
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.FeedBrowseSlotUiModel
import com.tokopedia.feedplus.browse.data.model.HeaderDetailModel
import com.tokopedia.feedplus.browse.data.model.StoryGroupsModel
import com.tokopedia.feedplus.browse.data.model.StoryNodeModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.model.WidgetRecommendationModel
import com.tokopedia.feedplus.browse.presentation.model.exception.RestrictedKeywordException
import com.tokopedia.feedplus.data.FeedXCard
import com.tokopedia.feedplus.data.FeedXHomeEntity
import com.tokopedia.feedplus.data.GetContentWidgetRecommendationResponse
import com.tokopedia.feedplus.presentation.model.type.AuthorType
import com.tokopedia.play.widget.ui.model.PartnerType
import com.tokopedia.play.widget.ui.model.PlayGridType
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelTypeTransition
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetPartnerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetShareUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetTotalView
import com.tokopedia.play.widget.ui.model.PlayWidgetVideoUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import com.tokopedia.stories.internal.model.StoriesGroupsResponseModel
import com.tokopedia.videoTabComponent.domain.mapper.FEED_TYPE_CHANNEL_BLOCK
import com.tokopedia.videoTabComponent.domain.mapper.FEED_TYPE_TAB_MENU
import javax.inject.Inject

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseMapper @Inject constructor() {

    fun mapHeaderData(response: FeedXHeaderResponse): HeaderDetailModel {
        return HeaderDetailModel.create(response.feedXHeaderData.data.browse)
    }

    internal fun mapSlotsResponse(response: FeedXHomeEntity): List<FeedBrowseSlotUiModel> {
        return response.items.mapNotNull { item ->
            if (item.typename == FeedXCard.TYPE_FEED_X_CARD_PLACEHOLDER) {
                if (item.type.startsWith(PREFIX_SLOT_WIDGET)) {
                    FeedBrowseSlotUiModel.ChannelsWithMenus(
                        slotId = item.id,
                        title = item.title,
                        group = item.type,
                        menus = emptyMap(),
                        selectedMenuId = ""
                    )
                } else if (item.type.startsWith(PREFIX_RECOMMENDATION_WIDGET)) {
                    when (val identifier = item.type.removePrefix("$PREFIX_RECOMMENDATION_WIDGET:")) {
                        IDENTIFIER_INSPIRATIONAL_WIDGET -> {
                            FeedBrowseSlotUiModel.InspirationBanner(
                                slotId = item.id,
                                title = item.title,
                                identifier = identifier,
                                bannerList = emptyList()
                            )
                        }
                        IDENTIFIER_UGC_WIDGET -> {
                            FeedBrowseSlotUiModel.Authors(
                                slotId = item.id,
                                title = item.title,
                                identifier = identifier,
                                authorList = emptyList()
                            )
                        }
                        else -> null
                    }
                } else if (item.type.startsWith(PREFIX_STORY_GROUPS_WIDGET)) {
                    FeedBrowseSlotUiModel.StoryGroups(
                        slotId = item.id,
                        title = item.title,
                        storyList = emptyList(),
                        nextCursor = "",
                        source = item.type.removePrefix("$PREFIX_STORY_GROUPS_WIDGET:")
                    )
                } else {
                    null
                }
            } else {
                null
            }
        }
    }

    internal fun mapWidgetResponse(response: WidgetSlot): ContentSlotModel {

        if (response.playGetContentSlot.meta.statusCode == FORBIDDEN_SEARCH_KEYWORD) {
            throw RestrictedKeywordException()
        }

        val data = response.playGetContentSlot.data
        val firstWidget = data.firstOrNull()
        val nextCursor = response.playGetContentSlot.meta.nextCursor
        return when (firstWidget?.type) {
            FEED_TYPE_TAB_MENU -> {
                ContentSlotModel.TabMenus(
                    firstWidget.items.map { item ->
                        WidgetMenuModel(
                            id = item.id,
                            label = item.label,
                            group = item.group,
                            sourceType = item.sourceType,
                            sourceId = item.sourceId
                        )
                    }
                )
            }
            FEED_TYPE_CHANNEL_BLOCK -> {
                ContentSlotModel.ChannelBlock(
                    firstWidget.title,
                    mapChannel(firstWidget),
                    mapPlayWidgetConfig(response.playGetContentSlot.meta),
                    nextCursor
                )
            }
            else -> {
                ContentSlotModel.NoData(nextCursor)
            }
        }
    }

    internal fun mapWidgetResponse(response: GetContentWidgetRecommendationResponse): WidgetRecommendationModel {
        val data = response.contentWidgetRecommendation.data
        val firstItem = data.firstOrNull() ?: return WidgetRecommendationModel.Empty
        return when (firstItem.typename) {
            "ContentWidgetAuthor" -> {
                WidgetRecommendationModel.Authors(
                    data.mapNotNull {
                        if (it.typename != "ContentWidgetAuthor") {
                            null
                        } else {
                            AuthorWidgetModel(
                                id = it.author.id,
                                name = it.author.name,
                                avatarUrl = it.author.thumbnailUrl,
                                coverUrl = it.media.coverUrl,
                                totalViewFmt = it.viewsFmt,
                                appLink = it.author.appLink,
                                contentId = it.contentID.id,
                                contentAppLink = it.appLink,
                                channelType = it.media.type
                            )
                        }
                    }
                )
            }
            "ContentWidgetBanner" -> {
                WidgetRecommendationModel.Banners(
                    data.mapNotNull {
                        if (it.typename != "ContentWidgetBanner") {
                            null
                        } else {
                            BannerWidgetModel(
                                title = it.title,
                                imageUrl = it.media.link,
                                appLink = it.appLink
                            )
                        }
                    }
                )
            }
            else -> WidgetRecommendationModel.Empty
        }
    }

    internal fun mapWidgetResponse(response: StoriesGroupsResponseModel): StoryGroupsModel {
        return StoryGroupsModel(
            storyList = response.data.groups.mapNotNull {
                if (!it.author.hasStory) return@mapNotNull null
                StoryNodeModel(
                    id = it.author.id,
                    name = it.name,
                    thumbnailUrl = it.image,
                    hasUnseenStory = it.author.isUnseenStoryExist,
                    appLink = it.appLink,
                    lastUpdatedAt = System.currentTimeMillis(),
                    authorType = AuthorType.Shop
                )
            },
            nextCursor = response.data.meta.nextCursor
        )
    }

    private fun mapChannel(data: Content): List<PlayWidgetChannelUiModel> {
        return data.items.map { item ->
            mapChannel(item)
        }
    }

    private fun mapPlayWidgetConfig(data: ContentSlotMeta): PlayWidgetConfigUiModel {
        return PlayWidgetConfigUiModel.Empty.copy(
            autoRefresh = data.autoRefresh,
            autoRefreshTimer = data.autoRefreshTimer,
            autoPlay = data.isAutoplay,
            autoPlayAmount = data.maxAutoplayInCell
        )
    }

    private fun mapChannel(
        item: ContentItem
    ): PlayWidgetChannelUiModel {
        val channelType = PlayWidgetChannelType.getByValue(item.airTime)
        val totalView = item.stats.view.formatted
        val coverUrl = item.coverUrl
        return PlayWidgetChannelUiModel(
            channelId = item.id,
            title = item.title,
            appLink = item.appLink,
            startTime = item.startTime,
            totalView = PlayWidgetTotalView(totalView, true),
            promoType = PlayWidgetPromoType.NoPromo,
            reminderType = PlayWidgetReminderType.NotReminded,
            partner = PlayWidgetPartnerUiModel(
                id = item.partner.id,
                name = item.partner.name,
                avatarUrl = item.partner.thumbnailUrl,
                badgeUrl = item.partner.badgeUrl,
                appLink = item.partner.appLink,
                type = PartnerType.Unknown
            ),
            video = PlayWidgetVideoUiModel.Empty.copy(coverUrl = coverUrl),
            channelType = channelType,
            hasGame = false,
            share = PlayWidgetShareUiModel.Empty,
            performanceSummaryLink = "",
            poolType = "",
            recommendationType = item.recommendationType,
            hasAction = false,
            products = emptyList(),
            shouldShowPerformanceDashboard = false,
            channelTypeTransition = PlayWidgetChannelTypeTransition(prevType = null, currentType = PlayWidgetChannelType.Unknown),
            gridType = PlayGridType.Unknown,
            extras = emptyMap()
        )
    }

    companion object {
        private const val PREFIX_SLOT_WIDGET = "browse_channel_slot"
        private const val PREFIX_RECOMMENDATION_WIDGET = "browse_widget_recommendation"
        private const val PREFIX_STORY_GROUPS_WIDGET = "browse_story_group"

        /**
         * Recommendation Identifier
         */
        private const val IDENTIFIER_UGC_WIDGET = "content_browse_ugc"
        private const val IDENTIFIER_INSPIRATIONAL_WIDGET = "content_browse_inspirational"

        private const val FORBIDDEN_SEARCH_KEYWORD = 435
    }
}
