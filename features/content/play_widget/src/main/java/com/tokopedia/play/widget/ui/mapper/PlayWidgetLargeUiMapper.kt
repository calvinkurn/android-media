package com.tokopedia.play.widget.ui.mapper

import com.tokopedia.play.widget.data.*
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play_common.transformer.DefaultHtmlTextTransformer
import com.tokopedia.play_common.util.datetime.PlayDateTimeFormatter
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by astidhiyaa on 18/01/22
 */
class PlayWidgetLargeUiMapper @Inject constructor(
    private val configMapper: PlayWidgetConfigMapper,
    private val promoLabelMapper: PlayWidgetPromoLabelMapper,
    private val videoMapper: PlayWidgetVideoMapper,
    private val userSession: UserSessionInterface
) : PlayWidgetMapper {

    private val htmlTextTransformer = DefaultHtmlTextTransformer()

    override fun mapWidget(data: PlayWidget, prevModel: PlayWidgetUiModel?): PlayWidgetUiModel {
        val prevLargeModel = prevModel as? PlayWidgetUiModel.Large

        return PlayWidgetUiModel.Large(
            title = data.meta.widgetTitle,
            actionTitle = data.meta.buttonText,
            actionAppLink = data.meta.buttonApplink,
            isActionVisible = data.meta.isButtonVisible,
            config = configMapper.mapWidgetConfig(data),
            items = mapWidgetItem(prevLargeModel?.items, data.data)
        )
    }

    private fun mapWidgetItem(
        prevItems: List<PlayWidgetLargeItemUiModel>?,
        items: List<PlayWidgetItem>
    ): List<PlayWidgetLargeItemUiModel> = items.mapNotNull {
        when (it.typename) {
            "banner" -> mapWidgetItemBanner(it)
            "channel" -> mapWidgetItemChannel(
                prevItems?.find { prevItem -> prevItem is PlayWidgetLargeChannelUiModel && prevItem.channelId == it.id } as? PlayWidgetLargeChannelUiModel,
                it
            )
            else -> null
        }
    }

    private fun mapWidgetItemBanner(item: PlayWidgetItem): PlayWidgetLargeBannerUiModel =
        PlayWidgetLargeBannerUiModel(
            appLink = item.appLink,
            webLink = item.webLink,
            imageUrl = item.backgroundUrl,
            partner = mapWidgetPartnerInfo(item.partner),
        )

    private fun mapWidgetItemChannel(
        prevItem: PlayWidgetLargeChannelUiModel?,
        item: PlayWidgetItem
    ): PlayWidgetLargeChannelUiModel {
        val channelType = PlayWidgetChannelType.getByValue(item.widgetType)

        return PlayWidgetLargeChannelUiModel(
            channelId = item.id,
            title = item.title,
            channelType = channelType,
            appLink = item.appLink,
            webLink = item.webLink,
            startTime = PlayDateTimeFormatter.formatDate(item.startTime),
            totalView = item.stats.view.formatted,
            totalViewVisible = item.video.isShowTotalView,
            promoType = promoLabelMapper.mapWidgetPromoType(item.config.promoLabels),
            reminderType = getReminderType(item.config.isReminderSet),
            partner = mapWidgetPartnerInfo(item.partner),
            video = videoMapper.mapWidgetItemVideo(item.video),
            hasAction = shouldHaveActionMenu(channelType, item.partner.id),
            channelTypeTransition = PlayWidgetChannelTypeTransition(
                prevType = prevItem?.channelType,
                currentType = channelType
            ),
            share = mapWidgetShare(item.share),
            performanceSummaryLink = item.performanceSummaryPageLink,
            hasGiveaway = promoLabelMapper.mapWidgetHasGiveaway(item.config.promoLabels),
            poolType = item.widgetSortingMethod,
            recommendationType = item.recommendationType,
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

    private fun shouldHaveActionMenu(
        channelType: PlayWidgetChannelType,
        partnerId: String
    ): Boolean {
        return channelType == PlayWidgetChannelType.Vod &&
                userSession.shopId == partnerId
    }

    private fun mapWidgetPartnerInfo(partner: PlayWidgetItemPartner) = PlayWidgetPartnerUiModel(
        id = partner.id,
        name = htmlTextTransformer.transform(partner.name)
    )
}