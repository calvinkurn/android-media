package com.tokopedia.people.utils

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalFeed
import com.tokopedia.people.model.PlayPostContentItem
import com.tokopedia.people.model.PostPromoLabel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelTypeTransition
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetPartnerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetShareUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetTotalView
import com.tokopedia.play.widget.ui.model.PlayWidgetVideoUiModel
import com.tokopedia.play.widget.ui.model.getReminderType
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType

private const val FEED_TYPE_PINNED_FEEDS = "pinnedFeeds"
private const val FEED_TYPE_CHANNEL_BLOCK = "channelBlock"
const val FEED_TYPE_TAB_MENU = "tabMenu"
private const val FEED_TYPE_CHANNEL_RECOM = "channelRecom"
private const val FEED_TYPE_CHANNEL_HIGHLIGHT = "channelHighlight"
private const val WIDGET_LIVE ="live"
const val WIDGET_UPCOMING ="upcoming"
private const val FEED_SEE_MORE_LIVE_APP_LINK = "${ApplinkConst.FEED_PlAY_LIVE_DETAIL}?${ApplinkConstInternalFeed.PLAY_LIVE_PARAM_WIDGET_TYPE}=${WIDGET_LIVE}"
private const val FEED_SEE_MORE_UPCOMING_APP_LINK = "${ApplinkConst.FEED_PlAY_LIVE_DETAIL}?${ApplinkConstInternalFeed.PLAY_LIVE_PARAM_WIDGET_TYPE}=${WIDGET_UPCOMING}"
private const val  UPCOMING_WIDGET_TITLE = "Nantikan live seru lainnya!"
private const val  LIVE_WIDGET_TITLE = "Lagi Live, nih!"
private const val HADIAH = "Hadiah"


object UserProfileVideoMapper {

    fun map(
        playSlotList: PlayPostContentItem,
        shopId: String
    ): PlayWidgetChannelUiModel {
        return getWidgetItemUiModel(playSlotList, shopId)

//        val list = mutableListOf<PlayWidgetUiModel>()


//        playSlotList.forEach { playSlot ->
//            when (playSlot.type) {
//                FEED_TYPE_PINNED_FEEDS -> {
//                    list.add(
//                        PlayWidgetJumboUiModel(
//                            getWidgetUiModel(playSlot, meta, shopId)
//                        )
//                    )
//                }
//                FEED_TYPE_CHANNEL_BLOCK -> {
//                    list.add(
//                        getWidgetUiModel(playSlot, meta, shopId)
//                    )
//                }
//                FEED_TYPE_TAB_MENU -> {
//                    list.add(
//                        PlaySlotTabMenuUiModel(
//                            playSlot.items.mapIndexed { index, playSlotItem ->
//                                PlaySlotTabMenuUiModel.Item(
//                                    playSlotItem.id, playSlotItem.label, playSlotItem.icon_url,
//                                    playSlotItem.group, playSlotItem.source_type,
//                                    playSlotItem.source_id, playSlotItem.slug_id, index == position
//                                )
//                            }
//                        )
//                    )
//                }
//                FEED_TYPE_CHANNEL_RECOM -> {
//                    list.add(
//                        PlayWidgetLargeUiModel(
//                            getWidgetUiModel(playSlot, meta, shopId)
//                        )
//
//                    )
//                }
//                FEED_TYPE_CHANNEL_HIGHLIGHT -> {
//                    list.add(
//                        PlayWidgetMediumUiModel(
//                            getWidgetUiModel(playSlot, meta, shopId)
//                        )
//
//                    )
//                }
//            }
//        }

//        return list
    }

//    private fun getWidgetUiModel(
//        playSlot: PlayPostContent,
//        shopId: String
//    ): PlayWidgetUiModel {
//
//        val item = playSlot.items.firstOrNull() ?: return PlayWidgetUiModel.Empty
//
//        //check these values
//
//        val autoRefresh = false
//        val autoRefreshTimer: Long = 0
//        val autoPlayAmount: Int = 8
//        val maxAutoPlayWifiDuration: Int = 30
//        val businessWidgetPosition: Int = 30
//        //till here
//
//
//        return PlayWidgetUiModel(
//            title = playSlot.title,
//            actionTitle = "",
//            actionAppLink = "",
//            isActionVisible = false,
//            config = PlayWidgetConfigUiModel(
//                autoRefresh, autoRefreshTimer, false, 0,
//                0, maxAutoPlayWifiDuration, businessWidgetPosition
//            ),
//            background = PlayWidgetBackgroundUiModel(
//                "", item.appLink, item.webLink, emptyList(), ""
//            ),
//            getWidgetItemUiModel(item, shopId)
//        )
//
//    }

    private fun getWidgetItemUiModel(item: PlayPostContentItem, shopId: String): PlayWidgetChannelUiModel{

        //check PlayWidgetShareUiModel(item.share.text -> is it be `item.share.text for "fullShareContent"`
        val performanceSummaryLink = ""
        val poolType = ""
        val recommendationType = ""

        val channelTypeTransitionPrev = ""
        val channelTypeTransitionNext = ""
        //till here
//
//        val list = mutableListOf<PlayWidgetItemUiModel>()
//
//        items.forEach { item ->
            val channelType = PlayWidgetChannelType.getByValue(item.airTime)
//            list.add(
                return PlayWidgetChannelUiModel(
                    channelId = item.id,
                    title = item.title,
                    appLink = item.appLink,
                    startTime = UserProfileDateTimeFormatter.formatDate(item.startTime),
                    totalView = PlayWidgetTotalView(item.stats.view.formatted,
                        false),
                    promoType = PlayWidgetPromoType.getByType(
                        "",
                        ""
                    ),
                    reminderType = getReminderType(item.configurations.reminder.isSet),
                    //TODO partner check later
                    partner = PlayWidgetPartnerUiModel("", ""),
                    video = PlayWidgetVideoUiModel(item.id, item.isLive, item.coverUrl, item.webLink),
                    channelType = channelType,
                    hasGiveaway = mapHasGiveaway(item.configurations.promoLabels),
                    //TODO later
                    share = PlayWidgetShareUiModel("", false),
                    performanceSummaryLink = performanceSummaryLink,
                    poolType = poolType,
                    recommendationType = recommendationType,
                    hasAction = shouldHaveActionMenu(channelType,item.id, shopId),
                    channelTypeTransition = PlayWidgetChannelTypeTransition(
                        PlayWidgetChannelType.getByValue(channelTypeTransitionPrev),
                        PlayWidgetChannelType.getByValue(channelTypeTransitionNext)
                    )
                )
//            )
        }
    }

    private fun mapHasGiveaway(promoLabels: List<PostPromoLabel>): Boolean {
        //TODO PostPromoLabel.type missing
        return promoLabels.firstOrNull { it.text == HADIAH } != null
    }

    private fun shouldHaveActionMenu(channelType: PlayWidgetChannelType, partnerId: String, shopId: String): Boolean {
        return channelType == PlayWidgetChannelType.Vod &&
                shopId == partnerId
    }