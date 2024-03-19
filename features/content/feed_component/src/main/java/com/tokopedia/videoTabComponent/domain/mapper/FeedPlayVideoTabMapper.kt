package com.tokopedia.videoTabComponent.domain.mapper

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalFeed
import com.tokopedia.play.widget.pref.PlayWidgetPreference
import com.tokopedia.play.widget.ui.model.PartnerType
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelTypeTransition
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetPartnerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetShareUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetTotalView
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetVideoUiModel
import com.tokopedia.play.widget.ui.model.getReminderType
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import com.tokopedia.videoTabComponent.domain.model.data.Configurations
import com.tokopedia.videoTabComponent.domain.model.data.PlayFeedUiModel
import com.tokopedia.videoTabComponent.domain.model.data.PlayGetContentSlotResponse
import com.tokopedia.videoTabComponent.domain.model.data.PlayPagingProperties
import com.tokopedia.videoTabComponent.domain.model.data.PlaySlot
import com.tokopedia.videoTabComponent.domain.model.data.PlaySlotItems
import com.tokopedia.videoTabComponent.domain.model.data.PlaySlotTabMenuUiModel
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetLargeUiModel
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetMediumUiModel
import com.tokopedia.videoTabComponent.util.PlayFeedDateTimeFormatter

private const val FEED_TYPE_PINNED_FEEDS = "pinnedFeeds"
const val FEED_TYPE_CHANNEL_BLOCK = "channelBlock"
const val FEED_TYPE_TAB_MENU = "tabMenu"
const val FEED_TYPE_CHANNEL_RECOM = "channelRecom"
private const val FEED_TYPE_CHANNEL_HIGHLIGHT = "channelHighlight"
private const val WIDGET_LIVE = "live"
const val WIDGET_UPCOMING = "upcoming"
private const val FEED_SEE_MORE_LIVE_APP_LINK = "${ApplinkConst.FEED_PlAY_LIVE_DETAIL}?${ApplinkConstInternalFeed.PLAY_LIVE_PARAM_WIDGET_TYPE}=$WIDGET_LIVE"
private const val FEED_SEE_MORE_UPCOMING_APP_LINK = "${ApplinkConst.FEED_PlAY_LIVE_DETAIL}?${ApplinkConstInternalFeed.PLAY_LIVE_PARAM_WIDGET_TYPE}=$WIDGET_UPCOMING"
private const val GIVEAWAY = "GIVEAWAY"

object FeedPlayVideoTabMapper {

    private const val AUTO_PLAY_AMOUNT = 8
    private const val MAX_AUTO_PLAY_WIFI_DURATION = 30
    private const val BUSINESS_WIDGET_POSITION = 30

    fun getTabData(playGetContentSlotResponse: PlayGetContentSlotResponse): List<PlaySlot> {
        return playGetContentSlotResponse.data.filter {
            it.type == FEED_TYPE_TAB_MENU
        }
    }

    fun map(
        playSlotList: List<PlaySlot>,
        meta: PlayPagingProperties,
        position: Int = 0,
        shopId: String,
        playWidgetPreference: PlayWidgetPreference
    ): List<PlayFeedUiModel> {
        val list = mutableListOf<PlayFeedUiModel>()

        playSlotList.forEach { playSlot ->
            when (playSlot.type) {
                FEED_TYPE_CHANNEL_BLOCK -> {
                    list.add(
                        PlayWidgetLargeUiModel(
                            getWidgetUiModel(playSlot, meta, shopId, playWidgetPreference)
                        )
                    )
                }
                FEED_TYPE_TAB_MENU -> {
                    list.add(
                        PlaySlotTabMenuUiModel(
                            playSlot.items.mapIndexed { index, playSlotItem ->
                                mapPlayTabData(playSlotItem, index == position)
                            }
                        )
                    )
                }
                FEED_TYPE_CHANNEL_RECOM -> {
                    list.add(
                        PlayWidgetLargeUiModel(
                            getWidgetUiModel(playSlot, meta, shopId, playWidgetPreference)
                        )

                    )
                }
                FEED_TYPE_CHANNEL_HIGHLIGHT -> {
                    list.add(
                        PlayWidgetMediumUiModel(
                            getWidgetUiModel(playSlot, meta, shopId, playWidgetPreference)
                        )

                    )
                }
            }
        }

        return list
    }

    private fun mapPlayTabData(playSlotItem: PlaySlotItems, isSelected: Boolean) = PlaySlotTabMenuUiModel.Item(
        playSlotItem.id,
        playSlotItem.label,
        playSlotItem.icon_url,
        playSlotItem.group,
        playSlotItem.source_type,
        playSlotItem.source_id,
        playSlotItem.slug_id,
        isSelected
    )

    private fun getWidgetUiModel(
        playSlot: PlaySlot,
        meta: PlayPagingProperties,
        shopId: String,
        playWidgetPreference: PlayWidgetPreference
    ): PlayWidgetUiModel {
        val item = playSlot.items.firstOrNull() ?: return PlayWidgetUiModel.Empty

        // check these values

        val autoRefresh = false
        val autoRefreshTimer: Long = 0

        // till here
        val actionLink = if (PlayWidgetChannelType.getByValue(playSlot.items.first().air_time) == PlayWidgetChannelType.Upcoming) FEED_SEE_MORE_UPCOMING_APP_LINK else FEED_SEE_MORE_LIVE_APP_LINK

        return PlayWidgetUiModel(
            title = playSlot.title,
            actionTitle = playSlot.lihat_semua.label,
            actionAppLink = actionLink,
            isActionVisible = playSlot.lihat_semua.show,
            config = PlayWidgetConfigUiModel(
                autoRefresh,
                autoRefreshTimer,
                playWidgetPreference.getAutoPlay(meta.is_autoplay),
                AUTO_PLAY_AMOUNT,
                meta.max_autoplay_in_cell,
                MAX_AUTO_PLAY_WIFI_DURATION,
                BUSINESS_WIDGET_POSITION
            ),
            background = PlayWidgetBackgroundUiModel(
                "",
                item.appLink,
                item.webLink,
                emptyList(),
                ""
            ),
            getWidgetItemUiModel(playSlot.items, shopId)
        )
    }

    private fun getWidgetItemUiModel(items: List<PlaySlotItems>, shopId: String): List<PlayWidgetItemUiModel> {
        // check PlayWidgetShareUiModel(item.share.text -> is it be `item.share.text for "fullShareContent"`
        val performanceSummaryLink = ""
        val poolType = ""
        val recommendationType = ""

        val channelTypeTransitionPrev = ""
        val channelTypeTransitionNext = ""
        // till here

        val list = mutableListOf<PlayWidgetItemUiModel>()

        items.forEach { item ->
            val channelType = PlayWidgetChannelType.getByValue(item.air_time)
            list.add(
                PlayWidgetChannelUiModel(
                    channelId = item.id,
                    title = item.title,
                    appLink = item.appLink,
                    startTime = PlayFeedDateTimeFormatter.formatDate(item.start_time),
                    totalView = PlayWidgetTotalView(item.stats.view.formatted, channelType != PlayWidgetChannelType.Upcoming),
                    promoType = PlayWidgetPromoType.getByType(
                        item.configurations.promoLabels.firstOrNull()?.type ?: "",
                        item.configurations.promoLabels.firstOrNull()?.text ?: ""
                    ),
                    reminderType = getReminderType(item.configurations.reminder.isSet),
                    partner = PlayWidgetPartnerUiModel(
                        id = item.partner.id,
                        name = MethodChecker.fromHtml(item.partner.name).toString(),
                        type = PartnerType.getTypeByValue(item.partner.name),
                        avatarUrl = item.partner.thumbnailUrl,
                        badgeUrl = item.partner.badgeUrl,
                        appLink = item.partner.appLink
                    ),
                    video = PlayWidgetVideoUiModel(
                        item.video.id,
                        item.is_live,
                        item.video.cover_url,
                        item.video.stream_source
                    ),
                    channelType = channelType,
                    hasGame = mapHasGame(item.configurations.promoLabels),
                    share = PlayWidgetShareUiModel(item.share.text, item.share.is_show_button),
                    performanceSummaryLink = performanceSummaryLink,
                    poolType = poolType,
                    recommendationType = recommendationType,
                    hasAction = shouldHaveActionMenu(channelType, item.partner.id, shopId),
                    shouldShowPerformanceDashboard = shouldShowPerformanceDashboard(
                        partnerType = item.partner.type,
                        partnerId = item.partner.id,
                        shopId = shopId
                    ),
                    channelTypeTransition = PlayWidgetChannelTypeTransition(
                        PlayWidgetChannelType.getByValue(channelTypeTransitionPrev),
                        PlayWidgetChannelType.getByValue(channelTypeTransitionNext)
                    ),
                    products = emptyList()
                )
            )
        }

        return list
    }
    private fun mapHasGame(promoLabels: List<Configurations.PromoLabel>): Boolean {
        return promoLabels.firstOrNull { it.type == GIVEAWAY } != null
    }

    private fun shouldHaveActionMenu(channelType: PlayWidgetChannelType, partnerId: String, shopId: String): Boolean {
        return channelType == PlayWidgetChannelType.Vod &&
            shopId == partnerId
    }

    private fun shouldShowPerformanceDashboard(partnerType: String, partnerId: String, shopId: String): Boolean {
        return partnerType == PartnerType.Shop.value && partnerId == shopId
    }
}
