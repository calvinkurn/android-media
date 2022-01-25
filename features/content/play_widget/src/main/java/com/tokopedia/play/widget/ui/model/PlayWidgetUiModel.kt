package com.tokopedia.play.widget.ui.model

import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType

/**
 * Created by kenny.hadisaputra on 21/01/22
 */

data class PlayWidgetUiModel(
    val title: String,
    val actionTitle: String,
    val actionAppLink: String,
    val isActionVisible: Boolean,
    val config: PlayWidgetConfigUiModel,
    val background: PlayWidgetBackgroundUiModel,
    val items: List<PlayWidgetItemUiModel>,
) {
    companion object {
        val Empty: PlayWidgetUiModel
            get() = PlayWidgetUiModel(
                title = "",
                actionTitle = "",
                actionAppLink = "",
                isActionVisible = false,
                config = PlayWidgetConfigUiModel.Empty,
                background = PlayWidgetBackgroundUiModel.Empty,
                items = emptyList(),
            )
    }
}

sealed class PlayWidgetItemUiModel

data class PlayWidgetBannerUiModel(
    val appLink: String,
    val imageUrl: String,
) : PlayWidgetItemUiModel()

data class PlayWidgetChannelUiModel(
    val channelId: String,
    val title: String,
    val appLink: String,
    val startTime: String,
    val totalView: PlayWidgetTotalView,
    val promoType: PlayWidgetPromoType,
    val reminderType: PlayWidgetReminderType,
    val partner: PlayWidgetPartnerUiModel,
    val video: PlayWidgetVideoUiModel,
    val widgetType: PlayWidgetChannelType,
    val hasGiveaway: Boolean,
    val share: PlayWidgetShareUiModel,
    val performanceSummaryLink: String,
    val poolType: String,
    val recommendationType: String,
    val hasAction: Boolean,
) : PlayWidgetItemUiModel() {

    val hasPromo: Boolean
        get() = promoType != PlayWidgetPromoType.NoPromo && promoType != PlayWidgetPromoType.Unknown
}

data class PlayWidgetTotalView(
    val totalViewFmt: String,
    val isVisible: Boolean,
)