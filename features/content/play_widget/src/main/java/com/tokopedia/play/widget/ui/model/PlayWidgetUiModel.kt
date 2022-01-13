package com.tokopedia.play.widget.ui.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.widget.analytic.ImpressionableModel

/**
 * Created by mzennis on 05/10/20.
 */
sealed class PlayWidgetUiModel {

    data class Small(
            val title: String,
            val actionTitle: String,
            val actionAppLink: String,
            val isActionVisible: Boolean,
            override val config: PlayWidgetConfigUiModel,
            val useHeader: Boolean,
            val items: List<PlayWidgetSmallItemUiModel>,
    ) : PlayWidgetUiModel(), PlayWidgetConfigProvider, ImpressionableModel {

        override val impressHolder = ImpressHolder()
    }

    data class Medium(
            val title: String,
            val actionTitle: String,
            val actionAppLink: String,
            val isActionVisible: Boolean,
            override val config: PlayWidgetConfigUiModel,
            val background: PlayWidgetBackgroundUiModel,
            val items: List<PlayWidgetMediumItemUiModel>,
    ) : PlayWidgetUiModel(), PlayWidgetConfigProvider, ImpressionableModel {

        override val impressHolder = ImpressHolder()
    }

    data class Large(
        val title: String,
        val actionTitle: String,
        val actionAppLink: String,
        val isActionVisible: Boolean,
        override val config: PlayWidgetConfigUiModel,
        val items: List<PlayWidgetLargeItemUiModel>,
    ) : PlayWidgetUiModel(), PlayWidgetConfigProvider, ImpressionableModel {

        override val impressHolder = ImpressHolder()
    }

    data class Jumbo(
        val title: String,
        val actionTitle: String,
        val actionAppLink: String,
        val isActionVisible: Boolean,
        override val config: PlayWidgetConfigUiModel,
        val items: List<PlayWidgetJumboItemUiModel>,
    ) : PlayWidgetUiModel(), PlayWidgetConfigProvider, ImpressionableModel {

        override val impressHolder = ImpressHolder()
    }

    object Placeholder : PlayWidgetUiModel()
}