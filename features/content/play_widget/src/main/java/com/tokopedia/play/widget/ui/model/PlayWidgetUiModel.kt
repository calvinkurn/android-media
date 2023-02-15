package com.tokopedia.play.widget.ui.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.widget.analytic.ImpressionableModel
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
) : ImpressionableModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    val hasAction: Boolean
        get() = isActionVisible && actionAppLink.isNotEmpty()

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
    val gridType: PlayGridType = PlayGridType.Unknown,
    ) : PlayWidgetItemUiModel(), ImpressionableModel {

    override val impressHolder: ImpressHolder = ImpressHolder()
}

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
    val channelType: PlayWidgetChannelType,
    val hasGame: Boolean,
    val share: PlayWidgetShareUiModel,
    val performanceSummaryLink: String,
    val poolType: String,
    val recommendationType: String,
    val hasAction: Boolean,
    val channelTypeTransition: PlayWidgetChannelTypeTransition,
    val gridType: PlayGridType = PlayGridType.Unknown,
) : PlayWidgetItemUiModel(), ImpressionableModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    val hasPromo: Boolean
        get() = promoType != PlayWidgetPromoType.NoPromo && promoType != PlayWidgetPromoType.Unknown

    val isUpcoming: Boolean
        get() = channelType == PlayWidgetChannelType.Upcoming
}

data class PlayWidgetTotalView(
    val totalViewFmt: String,
    val isVisible: Boolean,
)

object PlayCardShimmering: PlayWidgetItemUiModel()
