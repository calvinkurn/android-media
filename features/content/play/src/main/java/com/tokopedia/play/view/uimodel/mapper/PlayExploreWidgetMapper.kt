package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.content.common.model.Content
import com.tokopedia.content.common.model.WidgetSlot
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.view.uimodel.ChipWidgetUiModel
import com.tokopedia.play.view.uimodel.WidgetUiModel
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
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
                when(it.type){
                    TAB_MENU_TYPE -> add(mapChips(it))
                    SUB_SLOT_TYPE -> add(WidgetUiModel.SubSlotUiModel)
                    CHANNEL_BLOCK_TYPE -> add(mapWidgets(it))
                    else -> {}
                }
            }
            with(widgetSlot.playGetContentSlot.playGetContentSlot){
                add(WidgetUiModel.PageConfig(isAutoPlay = this.isAutoplay, cursor = this.nextCursor))
            }
        }
    }

    private fun mapChips(content: Content) : WidgetUiModel.TabMenuUiModel {
        val newList = content.items.map {
            ChipWidgetUiModel(
                group = it.group,
                sourceType = it.sourceType,
                sourceId = it.sourceId,
                text = it.label,
            )
        }
        return WidgetUiModel.TabMenuUiModel(items = newList)
    }

    private fun mapWidgets(content: Content) : WidgetUiModel.WidgetItemUiModel {
           return WidgetUiModel.WidgetItemUiModel(
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
                       businessWidgetPosition = 1,
                   ),
                   background = PlayWidgetBackgroundUiModel(overlayImageAppLink = "", overlayImageUrl = "", overlayImageWebLink = "", backgroundUrl = "", gradientColors = emptyList()),
                   items = content.items.map {
                       PlayWidgetChannelUiModel(
                           channelId = it.id,
                           title = it.title,
                           appLink = it.appLink,
                           startTime = it.startTime,
                           totalView = PlayWidgetTotalView(totalViewFmt = it.stats.view.formatted, isVisible = true),
                           promoType = PlayWidgetPromoType.getByType(it.configurations.promoLabels.firstOrNull()?.type.orEmpty(), it.configurations.promoLabels.firstOrNull()?.text.orEmpty()),
                           reminderType = PlayWidgetReminderType.NotReminded,
                           partner = PlayWidgetPartnerUiModel(it.partner.id, it.partner.name),
                           video = PlayWidgetVideoUiModel(it.video.id, it.isLive,it.coverUrl,it.video.streamUrl),
                           channelType = PlayWidgetChannelType.getByValue(it.video.type),
                           hasGame = false,
                           share = PlayWidgetShareUiModel(fullShareContent = "", isShow = false),
                           performanceSummaryLink = "",
                           poolType = "",
                           recommendationType = "",
                           hasAction = false,
                           channelTypeTransition = PlayWidgetChannelTypeTransition(PlayWidgetChannelType.Vod, PlayWidgetChannelType.Vod),
                       )
                   }
               )
           )
    }

    companion object {
        private val TAB_MENU_TYPE = "tabMenu"
        private val SUB_SLOT_TYPE = "subSlot"
        private val CHANNEL_BLOCK_TYPE = "channelBlock"
    }
}
