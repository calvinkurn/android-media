package com.tokopedia.play.widget.ui.mapper

import com.tokopedia.play.widget.PlayWidgetUiModel
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetItem
import com.tokopedia.play.widget.data.PlayWidgetItemVideo
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetCardItemType
import com.tokopedia.play.widget.ui.type.PlayWidgetCardType


/**
 * Created by mzennis on 06/10/20.
 */
object PlayWidgetUiMapper {

    fun mapWidget(data: PlayWidget): PlayWidgetUiModel = PlayWidgetUiModel(
            title = data.meta.widgetTitle,
            actionTitle = data.meta.buttonText,
            actionAppLink = data.meta.buttonApplink,
            actionWebLink = data.meta.overlayImageWebLink,
            background = mapWidgetBackground(data),
            config = mapWidgetConfig(data),
            items = mapWidgetItem(data.data)
    )

    private fun mapWidgetBackground(data: PlayWidget): PlayWidgetBackgroundUiModel = PlayWidgetBackgroundUiModel(
            overlayImageUrl = data.meta.overlayImage,
            overlayImageAppLink = data.meta.overlayImageAppLink,
            overlayImageWebLink = data.meta.overlayImageWebLink,
            gradientColors = data.meta.gradient,
            backgroundUrl = data.meta.widgetBackground
    )

    private fun mapWidgetConfig(data: PlayWidget): PlayWidgetConfigUiModel = PlayWidgetConfigUiModel(
            autoPlay = data.meta.autoplay,
            autoPlayAmount = data.meta.autoplayAmount,
            autoRefresh = data.meta.autoRefresh,
            autoRefreshTimer = data.meta.autoRefreshTimer,
            maxAutoPlayCard = data.meta.maxAutoplayCell
    )

    private fun mapWidgetItem(items: List<PlayWidgetItem>): List<PlayWidgetCardUiModel> = items.map { mapWidgetItem(it) }

    private fun mapWidgetItem(item: PlayWidgetItem): PlayWidgetCardUiModel = PlayWidgetCardUiModel(
            type = PlayWidgetCardType.getByValue(item.typename),
            card = mapWidgetCardItem(item)
    )

    private fun mapWidgetCardItem(item: PlayWidgetItem): PlayWidgetCardItemUiModel = PlayWidgetCardItemUiModel(
            channelId = item.id,
            title = item.title,
            cardType = PlayWidgetCardItemType.getByValue(item.widgetType),
            appLink = item.appLink,
            webLink = item.webLink,
            startTime = item.startTime,
            totalView = item.stats.view.formatted,
            totalViewVisible = item.video.isShowTotalView,
            hasPromo = item.config.hasPromo,
            activeReminder = item.config.isReminderSet,
            isLive = item.video.isLive,
            partner = PlayWidgetPartnerUiModel(item.partner.id, item.partner.name),
            video = mapWidgetItemVideo(item.video),
            backgroundUrl = item.backgroundUrl
    )

    private fun mapWidgetItemVideo(item: PlayWidgetItemVideo): PlayWidgetCardVideoUiModel = PlayWidgetCardVideoUiModel(
            id = item.id,
            coverUrl = item.coverUrl,
            videoUrl = item.streamSource
    )
}