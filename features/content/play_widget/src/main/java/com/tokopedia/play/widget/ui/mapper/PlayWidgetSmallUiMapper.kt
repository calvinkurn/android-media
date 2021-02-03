package com.tokopedia.play.widget.ui.mapper

import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetItem
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import javax.inject.Inject

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetSmallUiMapper @Inject constructor(
        private val configMapper: PlayWidgetConfigMapper,
        private val videoMapper: PlayWidgetVideoMapper
) : PlayWidgetMapper {

    override fun mapWidget(data: PlayWidget, prevModel: PlayWidgetUiModel?): PlayWidgetUiModel = PlayWidgetUiModel.Small(
            title = data.meta.widgetTitle,
            actionTitle = data.meta.buttonText,
            actionAppLink = data.meta.buttonApplink,
            isActionVisible = data.meta.isButtonVisible,
            config = configMapper.mapWidgetConfig(data),
            items = mapWidgetItem(data.data),
            useHeader = true
    )

    private fun mapWidgetItem(items: List<PlayWidgetItem>): List<PlayWidgetSmallItemUiModel> = items.mapNotNull {
        when (it.typename) {
            "PlayWidgetBanner" -> mapWidgetItemBanner(it)
            "PlayWidgetChannel" -> mapWidgetItemChannel(it)
            else -> null
        }
    }

    private fun mapWidgetItemBanner(item: PlayWidgetItem): PlayWidgetSmallBannerUiModel = PlayWidgetSmallBannerUiModel(
            appLink = item.appLink,
            webLink = item.webLink,
            imageUrl = item.backgroundUrl
    )

    private fun mapWidgetItemChannel(item: PlayWidgetItem): PlayWidgetSmallChannelUiModel = PlayWidgetSmallChannelUiModel(
            channelId = item.id,
            title = item.title,
            channelType = PlayWidgetChannelType.getByValue(item.widgetType),
            appLink = item.appLink,
            webLink = item.webLink,
            startTime = item.startTime,
            totalView = item.stats.view.formatted,
            totalViewVisible = item.video.isShowTotalView,
            hasPromo = item.config.hasPromo,
            video = videoMapper.mapWidgetItemVideo(item.video)
    )
}