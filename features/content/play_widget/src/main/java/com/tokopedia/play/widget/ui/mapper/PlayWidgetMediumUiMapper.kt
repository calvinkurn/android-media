package com.tokopedia.play.widget.ui.mapper

import com.tokopedia.config.GlobalConfig
import com.tokopedia.play.widget.data.*
import com.tokopedia.play.widget.domain.PlayWidgetReminderUseCase
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play_common.transformer.DefaultHtmlTextTransformer
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


/**
 * Created by mzennis on 07/10/20.
 */
class PlayWidgetMediumUiMapper @Inject constructor(
        private val configMapper: PlayWidgetConfigMapper,
        private val videoMapper: PlayWidgetVideoMapper,
        private val userSession: UserSessionInterface
) : PlayWidgetMapper {

    private val htmlTextTransformer = DefaultHtmlTextTransformer()

    override fun mapWidget(data: PlayWidget, prevModel: PlayWidgetUiModel?): PlayWidgetUiModel {
        val widgetBackground = mapWidgetBackground(data)
        val prevMediumModel = prevModel as? PlayWidgetUiModel.Medium

        return PlayWidgetUiModel.Medium(
                title = data.meta.widgetTitle,
                actionTitle = data.meta.buttonText,
                actionAppLink = data.meta.buttonApplink,
                isActionVisible = data.meta.isButtonVisible,
                background = widgetBackground,
                config = configMapper.mapWidgetConfig(data),
                items = mapWidgetItem(prevMediumModel?.items, data.data).toMutableList().apply {
                    if (shouldAddLeftBanner(widgetBackground)) add(0, mapWidgetItemOverlay(widgetBackground))
                }
        )
    }

    fun mapWidgetToggleReminder(data: PlayWidgetReminder) = data.playToggleChannelReminder.header.status == PlayWidgetReminderUseCase.RESPONSE_STATUS_SUCCESS

    private fun mapWidgetBackground(data: PlayWidget): PlayWidgetBackgroundUiModel = PlayWidgetBackgroundUiModel(
            overlayImageUrl = data.meta.overlayImage,
            overlayImageAppLink = data.meta.overlayImageAppLink,
            overlayImageWebLink = data.meta.overlayImageWebLink,
            gradientColors = data.meta.gradient,
            backgroundUrl = data.meta.widgetBackground
    )

    private fun mapWidgetItem(prevItems: List<PlayWidgetMediumItemUiModel>?, items: List<PlayWidgetItem>): List<PlayWidgetMediumItemUiModel> = items.mapNotNull {
        when (it.typename) {
            "PlayWidgetBanner" -> mapWidgetItemBanner(it)
            "PlayWidgetChannel" -> mapWidgetItemChannel(
                    prevItems?.find { prevItem -> prevItem is PlayWidgetMediumChannelUiModel && prevItem.channelId == it.id } as? PlayWidgetMediumChannelUiModel,
                    it
            )
            else -> null
        }
    }

    private fun shouldAddLeftBanner(item: PlayWidgetBackgroundUiModel) = item.overlayImageUrl.isNotBlank()

    private fun mapWidgetItemOverlay(item: PlayWidgetBackgroundUiModel): PlayWidgetMediumOverlayUiModel = PlayWidgetMediumOverlayUiModel(
            appLink = item.overlayImageAppLink,
            webLink = item.overlayImageWebLink,
            imageUrl = item.overlayImageUrl
    )

    private fun mapWidgetItemBanner(item: PlayWidgetItem): PlayWidgetMediumBannerUiModel = PlayWidgetMediumBannerUiModel(
            appLink = item.appLink,
            webLink = item.webLink,
            imageUrl = item.backgroundUrl,
            partner = mapWidgetPartnerInfo(item.partner),
    )

    private fun mapWidgetItemChannel(prevItem: PlayWidgetMediumChannelUiModel?, item: PlayWidgetItem): PlayWidgetMediumChannelUiModel {
        val channelType = PlayWidgetChannelType.getByValue(item.widgetType)

        return PlayWidgetMediumChannelUiModel(
                channelId = item.id,
                title = item.title,
                channelType = channelType,
                appLink = item.appLink,
                webLink = item.webLink,
                startTime = item.startTime,
                totalView = item.stats.view.formatted,
                totalViewVisible = item.video.isShowTotalView,
                hasPromo = item.config.hasPromo,
                reminderType = getReminderType(item.config.isReminderSet),
                partner = mapWidgetPartnerInfo(item.partner),
                video = videoMapper.mapWidgetItemVideo(item.video),
                hasAction = shouldHaveActionMenu(channelType, item.partner.id),
                channelTypeTransition = PlayWidgetChannelTypeTransition(prevType = prevItem?.channelType, currentType = channelType),
                share = mapWidgetShare(item.share),
                performanceSummaryLink = item.performanceSummaryPageLink
        )
    }

    private fun mapWidgetShare(item: PlayWidgetItemShare): PlayWidgetShareUiModel {
        val fullShareContent = try {
            item.text.replace("${'$'}{url}", item.redirectUrl)
        } catch (e: Throwable) {
            "${item.text}/n${item.redirectUrl}"
        }

        return PlayWidgetShareUiModel(
                fullShareContent = fullShareContent,
                isShow = item.isShowButton && item.redirectUrl.isNotBlank()
        )
    }

    private fun shouldHaveActionMenu(channelType: PlayWidgetChannelType, partnerId: String): Boolean {
        return channelType == PlayWidgetChannelType.Vod &&
                GlobalConfig.isSellerApp() &&
                userSession.shopId == partnerId
    }

    private fun mapWidgetPartnerInfo(partner: PlayWidgetItemPartner) = PlayWidgetPartnerUiModel(
            id = partner.id,
            name = htmlTextTransformer.transform(partner.name)
    )
}