package com.tokopedia.play.widget.ui.mapper

import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetItem
import com.tokopedia.play.widget.data.PlayWidgetItemVideo
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType


/**
 * Created by mzennis on 06/10/20.
 */
object PlayWidgetUiMapper {

    fun mapWidget(data: PlayWidget): PlayWidgetMediumUiModel = PlayWidgetMediumUiModel(
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

    private fun mapWidgetItem(items: List<PlayWidgetItem>): List<PlayWidgetMediumItemUiModel> = items.mapNotNull {
        when (it.typename) {
            "PlayWidgetBanner" -> mapWidgetItemBanner(it)
            "PlayWidgetChannel" -> mapWidgetItemChannel(it)
            else -> null
        }
    }

    private fun mapWidgetItemBanner(item: PlayWidgetItem): PlayWidgetMediumBannerUiModel = PlayWidgetMediumBannerUiModel(
            appLink = item.appLink,
            webLink = item.webLink,
            imageUrl = item.backgroundUrl
    )

    private fun mapWidgetItemChannel(item: PlayWidgetItem): PlayWidgetMediumChannelUiModel = PlayWidgetMediumChannelUiModel(
            channelId = item.id,
            title = item.title,
            channelType = PlayWidgetChannelType.getByValue(item.widgetType),
            appLink = item.appLink,
            webLink = item.webLink,
            startTime = item.startTime,
            totalView = item.stats.view.formatted,
            totalViewVisible = item.video.isShowTotalView,
            hasPromo = item.config.hasPromo,
            activeReminder = item.config.isReminderSet,
            partner = PlayWidgetPartnerUiModel(item.partner.id, item.partner.name),
            video = mapWidgetItemVideo(item.video)
    )

    private fun mapWidgetItemVideo(item: PlayWidgetItemVideo): PlayWidgetVideoUiModel = PlayWidgetVideoUiModel(
            id = item.id,
            coverUrl = item.coverUrl,
            isLive = item.isLive,
            videoUrl = item.streamSource
    )
}