package com.tokopedia.play.widget.ui.mapper

import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetItem
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType


/**
 * Created by mzennis on 07/10/20.
 */
class PlayWidgetMediumUiMapper (
        private val configMapper: PlayWidgetConfigMapper,
        private val videoMapper: PlayWidgetVideoMapper
) : PlayWidgetMapper {

    override fun mapWidget(data: PlayWidget): PlayWidgetUiModel = PlayWidgetMediumUiModel(
            title = data.meta.widgetTitle,
            actionTitle = data.meta.buttonText,
            actionAppLink = data.meta.buttonApplink,
            actionWebLink = data.meta.overlayImageWebLink,
            background = mapWidgetBackground(data),
            config = configMapper.mapWidgetConfig(data),
            items = mapWidgetItem(data.data)
    )

    private fun mapWidgetBackground(data: PlayWidget): PlayWidgetBackgroundUiModel = PlayWidgetBackgroundUiModel(
            overlayImageUrl = data.meta.overlayImage,
            overlayImageAppLink = data.meta.overlayImageAppLink,
            overlayImageWebLink = data.meta.overlayImageWebLink,
            gradientColors = data.meta.gradient,
            backgroundUrl = data.meta.widgetBackground
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
            video = videoMapper.mapWidgetItemVideo(item.video)
    )
}