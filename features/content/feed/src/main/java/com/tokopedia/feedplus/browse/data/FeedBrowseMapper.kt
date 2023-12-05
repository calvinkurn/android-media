package com.tokopedia.feedplus.browse.data

import com.tokopedia.content.common.model.Content
import com.tokopedia.content.common.model.ContentItem
import com.tokopedia.content.common.model.ContentSlotMeta
import com.tokopedia.content.common.model.FeedXHeaderResponse
import com.tokopedia.content.common.model.WidgetSlot
import com.tokopedia.feedplus.browse.data.model.BannerWidgetModel
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.FeedBrowseSlotUiModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.model.WidgetRecommendationModel
import com.tokopedia.feedplus.data.FeedXCard
import com.tokopedia.feedplus.data.FeedXHomeEntity
import com.tokopedia.feedplus.data.GetContentWidgetRecommendationResponse
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
import com.tokopedia.videoTabComponent.domain.mapper.FEED_TYPE_CHANNEL_BLOCK
import com.tokopedia.videoTabComponent.domain.mapper.FEED_TYPE_TAB_MENU
import javax.inject.Inject

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseMapper @Inject constructor() {

    fun mapTitle(response: FeedXHeaderResponse): String {
        return response.feedXHeaderData.data.browse.title
    }

    internal fun mapSlotsResponse(response: FeedXHomeEntity): List<FeedBrowseSlotUiModel> {
        return response.items.mapNotNull { item ->
            if (item.typename == FeedXCard.TYPE_FEED_X_CARD_PLACEHOLDER) {
                if (item.type.startsWith("browse_channel_slot")) {
                    FeedBrowseSlotUiModel.ChannelsWithMenus(
                        slotId = item.id,
                        title = item.title,
                        group = item.type,
                        menus = emptyMap(),
                        selectedMenuId = ""
                    )
                } else if (item.type.startsWith("browse_widget_recommendation")) {
                    // UGC or Creator widget also one of the widgets with item.type = 'browse_widget_recommendation:ugc_widget'
                    FeedBrowseSlotUiModel.InspirationBanner(
                        slotId = item.id,
                        title = item.title,
                        identifier = item.type.removePrefix("browse_widget_recommendation"),
                        bannerList = emptyList(),
                        isLoading = false,
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
                WidgetRecommendationModel.Authors(emptyList())
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
}
