package com.tokopedia.play.widget.ui.mapper

import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetItem
import com.tokopedia.play.widget.data.PlayWidgetItemPartner
import com.tokopedia.play.widget.data.PlayWidgetItemShare
import com.tokopedia.play.widget.data.PlayWidgetItemVideo
import com.tokopedia.play.widget.data.PlayWidgetPromoLabel
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
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
import com.tokopedia.play_common.transformer.DefaultHtmlTextTransformer
import com.tokopedia.play_common.util.datetime.PlayDateTimeFormatter
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 21/01/22
 */

class PlayWidgetUiMapper @Inject constructor(
    private val userSession: UserSessionInterface
) {

    private val htmlTextTransformer = DefaultHtmlTextTransformer()

    fun mapWidget(data: PlayWidget): PlayWidgetUiModel {
        val widgetBackground = mapWidgetBackground(data)

        return PlayWidgetUiModel(
            title = data.meta.widgetTitle,
            actionTitle = data.meta.buttonText,
            actionAppLink = data.meta.buttonApplink,
            isActionVisible = data.meta.isButtonVisible,
            background = widgetBackground,
            config = mapWidgetConfig(data),
            items = mapWidgetItem(data.data),
        )
    }

    private fun mapWidgetConfig(data: PlayWidget): PlayWidgetConfigUiModel = PlayWidgetConfigUiModel(
        autoPlay = data.meta.autoplay,
        autoPlayAmount = data.meta.autoplayAmount,
        autoRefresh = data.meta.autoRefresh,
        autoRefreshTimer = data.meta.autoRefreshTimer,
        maxAutoPlayCellularDuration = data.meta.maxAutoplayCell,
        maxAutoPlayWifiDuration = data.meta.maxAutoplayWifi,
        businessWidgetPosition = data.meta.businessWidgetPosition
    )

    private fun mapWidgetBackground(data: PlayWidget): PlayWidgetBackgroundUiModel = PlayWidgetBackgroundUiModel(
        overlayImageUrl = data.meta.overlayImage,
        overlayImageAppLink = data.meta.overlayImageAppLink,
        overlayImageWebLink = data.meta.overlayImageWebLink,
        gradientColors = data.meta.gradient,
        backgroundUrl = data.meta.widgetBackground
    )

    private fun mapWidgetItem(items: List<PlayWidgetItem>): List<PlayWidgetItemUiModel> = items.mapNotNull {
        when (it.typename) {
            "PlayWidgetBanner" -> mapWidgetItemBanner(it)
            "PlayWidgetChannel" -> mapWidgetItemChannel(it)
            else -> null
        }
    }

    private fun mapWidgetItemBanner(item: PlayWidgetItem) = PlayWidgetBannerUiModel(
        appLink = item.appLink,
        imageUrl = item.backgroundUrl
    )

    private fun mapWidgetItemChannel(item: PlayWidgetItem): PlayWidgetChannelUiModel {
        val widgetType = PlayWidgetChannelType.getByValue(item.widgetType)
        return PlayWidgetChannelUiModel(
            channelId = item.id,
            title = item.title,
            appLink = item.appLink,
            startTime = PlayDateTimeFormatter.formatDate(item.startTime),
            totalView = mapTotalView(item),
            promoType = mapPromoType(item.config.promoLabels),
            reminderType = getReminderType(item.config.isReminderSet),
            partner = mapPartnerInfo(item.partner),
            video = mapVideo(item.video),
            widgetType = widgetType,
            hasGiveaway = mapHasGiveaway(item.config.promoLabels),
            share = mapShare(item.share),
            performanceSummaryLink = item.performanceSummaryPageLink,
            poolType = item.widgetSortingMethod,
            recommendationType = item.recommendationType,
            hasAction = shouldHaveActionMenu(widgetType, item.partner.id),
        )
    }

    private fun mapTotalView(item: PlayWidgetItem) = PlayWidgetTotalView(
        totalViewFmt = item.stats.view.formatted,
        isVisible = item.video.isShowTotalView,
    )

    private fun mapPromoType(promoLabels: List<PlayWidgetPromoLabel>): PlayWidgetPromoType {
        val promoLabel = promoLabels.firstOrNull { it.type != GIVEAWAY } ?: return PlayWidgetPromoType.NoPromo
        return PlayWidgetPromoType.getByType(promoLabel.type, promoLabel.text)
    }

    private fun mapHasGiveaway(promoLabels: List<PlayWidgetPromoLabel>): Boolean {
        return promoLabels.firstOrNull { it.type == GIVEAWAY } != null
    }

    private fun mapVideo(item: PlayWidgetItemVideo) = PlayWidgetVideoUiModel(
        id = item.id,
        coverUrl = item.coverUrl,
        isLive = item.isLive,
        videoUrl = item.streamSource
    )

    private fun mapPartnerInfo(partner: PlayWidgetItemPartner) = PlayWidgetPartnerUiModel(
        id = partner.id,
        name = htmlTextTransformer.transform(partner.name)
    )

    private fun mapShare(item: PlayWidgetItemShare): PlayWidgetShareUiModel {
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
                userSession.shopId == partnerId
    }

    companion object {
        private const val GIVEAWAY = "GIVEAWAY"
    }
}