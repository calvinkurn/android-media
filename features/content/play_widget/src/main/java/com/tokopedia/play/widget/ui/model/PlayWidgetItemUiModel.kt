package com.tokopedia.play.widget.ui.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.widget.analytic.ImpressionableModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * Created by jegul on 07/10/20
 */
sealed class PlayWidgetItemUiModel

/**
 * Small
 */
sealed class PlayWidgetSmallItemUiModel : PlayWidgetItemUiModel()

data class PlayWidgetSmallBannerUiModel(
        val imageUrl: String,
        val appLink: String,
        val webLink: String
) : PlayWidgetSmallItemUiModel()

data class PlayWidgetSmallChannelUiModel(
        val channelId: String,
        val title: String,
        val appLink: String,
        val webLink: String,
        val startTime: String,
        val totalView: String,
        val totalViewVisible: Boolean,
        val hasPromo: Boolean,
        val video: PlayWidgetVideoUiModel,
        val channelType: PlayWidgetChannelType
): PlayWidgetSmallItemUiModel(), ImpressionableModel {

    override val impressHolder = ImpressHolder()
}

/**
 * Medium
 */
sealed class PlayWidgetMediumItemUiModel: PlayWidgetItemUiModel()

data class PlayWidgetMediumOverlayUiModel(
        val imageUrl: String,
        val appLink: String,
        val webLink: String
) : PlayWidgetMediumItemUiModel(), ImpressionableModel {

    override val impressHolder = ImpressHolder()
}

data class PlayWidgetMediumBannerUiModel(
        val imageUrl: String,
        val appLink: String,
        val webLink: String,
        val partner: PlayWidgetPartnerUiModel
) : PlayWidgetMediumItemUiModel()

data class PlayWidgetMediumChannelUiModel(
        val channelId: String,
        val title: String,
        val appLink: String,
        val webLink: String,
        val startTime: String,
        val totalView: String,
        val totalViewVisible: Boolean,
        val hasPromo: Boolean,
        val reminderType: PlayWidgetReminderType,
        val partner: PlayWidgetPartnerUiModel,
        val video: PlayWidgetVideoUiModel,
        val channelType: PlayWidgetChannelType,
        val hasAction: Boolean,
        val channelTypeTransition: PlayWidgetChannelTypeTransition,
        val share: PlayWidgetShareUiModel,
        val performanceSummaryLink: String
) : PlayWidgetMediumItemUiModel(), ImpressionableModel {

    override val impressHolder = ImpressHolder()
}