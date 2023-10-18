package com.tokopedia.feedplus.browse.data

import com.tokopedia.content.common.model.Content
import com.tokopedia.content.common.model.ContentItem
import com.tokopedia.content.common.model.ContentSlotMeta
import com.tokopedia.content.common.model.FeedXHeaderResponse
import com.tokopedia.content.common.model.WidgetSlot
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.FeedBrowseModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.model.ChannelUiState
import com.tokopedia.feedplus.browse.presentation.model.ChipUiState
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseConfigUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.data.FeedXCard
import com.tokopedia.feedplus.data.FeedXHomeEntity
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
import com.tokopedia.videoTabComponent.domain.mapper.FEED_TYPE_CHANNEL_RECOM
import com.tokopedia.videoTabComponent.domain.mapper.FEED_TYPE_TAB_MENU
import javax.inject.Inject

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseMapper @Inject constructor() {

    fun mapTitle(response: FeedXHeaderResponse): String {
        return response.feedXHeaderData.data.browse.title
    }

    internal fun mapSlotsResponse(response: FeedXHomeEntity): List<FeedBrowseModel> {
        return response.items.mapNotNull { item ->
            if (item.typename == FeedXCard.TYPE_FEED_X_CARD_PLACEHOLDER) {
                if (item.type.startsWith("browse_channel_slot")) {
                    FeedBrowseModel.ChannelsWithMenus(
                        slotId = item.id,
                        title = item.title,
                        group = item.type,
                        menus = emptyMap(),
                        selectedMenuId = "",
                        type = FeedBrowseModel.ChannelsWithMenus.Type.Unknown,
                    )
                } else if (item.type.startsWith("browse_widget_recommendation")) {
                    FeedBrowseModel.InspirationBanner(
                        slotId = item.id,
                        title = item.title,
                        identifier = item.type,
                        bannerList = emptyList(),
                    )
                } else {
                    null
                }
            } else {
                null
            }
        }
    }

    fun mapSlots(response: FeedXHomeEntity): List<FeedBrowseUiModel> {
//        return response.items.mapNotNull { item ->
//            if (item.typename == FeedXCard.TYPE_FEED_X_CARD_PLACEHOLDER) {
//                FeedBrowseUiModel.Channel(
//                    id = item.id,
//                    title = item.title,
//                    extraParam = WidgetRequestModel(group = item.type),
//                    chipUiState = ChipUiState.Placeholder,
//                    channelUiState = ChannelUiState.Placeholder
//                )
//            } else {
//                null
//            }
//        }

        val slots = response.items.mapNotNull { item ->
            if (item.typename == FeedXCard.TYPE_FEED_X_CARD_PLACEHOLDER) {
                FeedBrowseUiModel.Channel(
                    id = item.id,
                    title = item.title,
                    extraParam = WidgetRequestModel(group = item.type),
                    chipUiState = ChipUiState.Placeholder,
                    channelUiState = ChannelUiState.Placeholder
                )
            } else {
                null
            }
        }

        return buildList {
            add(
                FeedBrowseUiModel.Title(
                    slotId = "1",
                    title = "Inspirasi ide belanja \uD83D\uDCA1"
                )
            )
            addAll(
                List(5) {
                    listOf(
                        FeedBrowseUiModel.Banner(
                            slotId = it.toString(),
                            title = "Buku",
                            imageUrl = "https://t4.ftcdn.net/jpg/01/77/47/67/360_F_177476718_VWfYMWCzK32bfPI308wZljGHvAUYSJcn.jpg",
                            appLink = "",
                        ),
                        FeedBrowseUiModel.Banner(
                            slotId = it.toString(),
                            title = "Dapur",
                            imageUrl = "https://t3.ftcdn.net/jpg/00/89/98/90/360_F_89989031_L8rIrutZm7gnGsIkkyDEAO9s43BYipqt.jpg",
                            appLink = "",
                        ),
                        FeedBrowseUiModel.Banner(
                            slotId = it.toString(),
                            title = "Elektronik",
                            imageUrl = "https://t4.ftcdn.net/jpg/00/86/56/65/240_F_86566504_1yU5DuXgM97XOXkZwcsSAvjP9EXtB4v2.jpg",
                            appLink = "",
                        ),
                        FeedBrowseUiModel.Banner(
                            slotId = it.toString(),
                            title = "Fashion Wanita",
                            imageUrl = "https://t3.ftcdn.net/jpg/03/67/86/38/360_F_367863869_tYibIxEubHFxMl33Ow38JAHYakAZGsNz.jpg",
                            appLink = "",
                        ),
                        FeedBrowseUiModel.Banner(
                            slotId = it.toString(),
                            title = "Fashion Anak",
                            imageUrl = "https://t3.ftcdn.net/jpg/02/47/59/52/360_F_247595262_LGff0kg2mdraH9DOqPGlftfsZCACc6Fn.jpg",
                            appLink = "",
                        ),
                    )
                }.flatten()
            )
            addAll(slots)
        }
    }

    fun mapWidget(response: WidgetSlot): FeedBrowseItemUiModel {
        val data = response.playGetContentSlot.data
        val firstWidget = data.first()
        return when (firstWidget.type) {
            FEED_TYPE_TAB_MENU -> {
                ChipUiState.Data(mapChips(firstWidget))
            }
            FEED_TYPE_CHANNEL_BLOCK -> {
                ChannelUiState.Data(
                    mapChannel(firstWidget),
                    mapConfig(response.playGetContentSlot.playGetContentSlot)
                )
            }
            else -> ChannelUiState.Error(IllegalStateException())
        }
    }

    internal fun mapWidgetResponse(response: WidgetSlot): ContentSlotModel {
        val data = response.playGetContentSlot.data
        val firstWidget = data.firstOrNull()
        return when (firstWidget?.type) {
            FEED_TYPE_TAB_MENU -> {
                ContentSlotModel.TabMenus(
                    firstWidget.items.map { item ->
                        WidgetMenuModel(
                            id = item.id,
                            label = item.label,
                            group = item.group,
                            sourceType = item.sourceType,
                            sourceId = item.sourceId,
                        )
                    }
                )
            }
            FEED_TYPE_CHANNEL_BLOCK -> {
//                ContentSlotModel.ChannelBlock(mapChannel(firstWidget))
                ContentSlotModel.ChannelRecommendation(mapChannel(firstWidget))
            }
            FEED_TYPE_CHANNEL_RECOM -> {
                ContentSlotModel.ChannelRecommendation(mapChannel(firstWidget))
            }
            else -> error("Type ${firstWidget?.type} is not currently supported")
        }
    }

    private fun mapChips(data: Content): List<FeedBrowseChipUiModel> {
        return data.items.mapIndexed { index, item ->
            FeedBrowseChipUiModel(
                id = item.id,
                label = item.label,
                extraParam = WidgetRequestModel(
                    group = item.group,
                    sourceType = item.sourceType,
                    sourceId = item.sourceId
                ),
                isSelected = index == 0 // select first chip
            )
        }
    }

    private fun mapChannel(data: Content): List<PlayWidgetChannelUiModel> {
        return data.items.map { item ->
            mapChannel(item)
        }
    }

    private fun mapConfig(data: ContentSlotMeta): FeedBrowseConfigUiModel {
        return FeedBrowseConfigUiModel(
            data = PlayWidgetConfigUiModel.Empty.copy(
                autoRefresh = data.autoRefresh,
                autoRefreshTimer = data.autoRefreshTimer,
                autoPlay = data.isAutoplay,
                autoPlayAmount = data.maxAutoplayInCell
            ),
            lastUpdated = System.currentTimeMillis()
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
                type = PartnerType.Unknown,
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
