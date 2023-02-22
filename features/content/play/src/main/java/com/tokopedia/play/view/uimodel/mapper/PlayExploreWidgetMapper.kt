package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.model.Content
import com.tokopedia.content.common.model.WidgetSlot
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.util.datetime.PlayDateTimeFormatter
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject

/**
 * @author by astidhiyaa on 30/11/22
 */
@PlayScope
class PlayExploreWidgetMapper @Inject constructor() {

    @OptIn(ExperimentalStdlibApi::class)
    fun map(widgetSlot: WidgetSlot): List<WidgetUiModel> {
        return buildList {
            widgetSlot.playGetContentSlot.data.map {
                when (it.type) {
                    TAB_MENU_TYPE -> add(mapChips(it))
                    SUB_SLOT_TYPE -> add(SubSlotUiModel)
                    CHANNEL_BLOCK_TYPE -> add(mapWidgets(it))
                    else -> {}
                }
            }
            with(widgetSlot.playGetContentSlot.playGetContentSlot) {
                add(PageConfig(isAutoPlay = this.isAutoplay, cursor = this.nextCursor))
            }
        }
    }

    private fun mapChips(content: Content): TabMenuUiModel {
        val newList = content.items.map {
            ChipWidgetUiModel(
                group = it.group,
                sourceType = it.sourceType,
                sourceId = it.sourceId,
                text = it.label
            )
        }
        return TabMenuUiModel(items = newList, state = ResultState.Success)
    }

    private val generatedId = AtomicLong(0)

    private fun mapWidgets(content: Content): WidgetItemUiModel {
        return WidgetItemUiModel(
            id = generatedId.getAndIncrement(),
            item =
            PlayWidgetUiModel(
                title = content.title,
                actionAppLink = "",
                actionTitle = content.title,
                isActionVisible = false,
                config = PlayWidgetConfigUiModel(
                    autoPlay = false,
                    autoRefresh = false,
                    autoPlayAmount = 0,
                    autoRefreshTimer = 0,
                    maxAutoPlayWifiDuration = 0,
                    maxAutoPlayCellularDuration = 0,
                    businessWidgetPosition = 1
                ),
                background = PlayWidgetBackgroundUiModel(overlayImageAppLink = "", overlayImageUrl = "", overlayImageWebLink = "", backgroundUrl = "", gradientColors = emptyList()),
                items = content.items.map {
                    val channelType = PlayWidgetChannelType.getByValue(it.airTime)
                    val partnerName = MethodChecker.fromHtml(it.partner.name).toString()
                    PlayWidgetChannelUiModel(
                        channelId = it.id,
                        title = it.title,
                        appLink = it.appLink,
                        startTime = PlayDateTimeFormatter.formatDate(it.startTime),
                        totalView = PlayWidgetTotalView(totalViewFmt = it.stats.view.formatted, isVisible = channelType != PlayWidgetChannelType.Upcoming),
                        promoType = PlayWidgetPromoType.getByType(it.configurations.promoLabels.firstOrNull()?.type.orEmpty(), it.configurations.promoLabels.firstOrNull()?.text.orEmpty()),
                        reminderType = getReminderType(it.configurations.reminder.isSet),
                        partner = PlayWidgetPartnerUiModel(it.partner.id, partnerName),
                        video = PlayWidgetVideoUiModel(it.video.id, it.isLive, it.coverUrl, it.video.streamUrl),
                        channelType = channelType,
                        hasGame = it.configurations.promoLabels.firstOrNull { it.type == GIVEAWAY } != null,
                        share = PlayWidgetShareUiModel(fullShareContent = "", isShow = false),
                        performanceSummaryLink = "",
                        poolType = "",
                        recommendationType = it.recommendationType,
                        hasAction = false,
                        channelTypeTransition = PlayWidgetChannelTypeTransition(PlayWidgetChannelType.Unknown, PlayWidgetChannelType.Unknown),
                        gridType = PlayGridType.Medium,
                    )
                }
            )
        )
    }

    companion object {
        private const val TAB_MENU_TYPE = "tabMenu"
        private const val SUB_SLOT_TYPE = "subSlot"
        private const val CHANNEL_BLOCK_TYPE = "channelBlock"

        private const val GIVEAWAY = "GIVEAWAY"
    }
}
