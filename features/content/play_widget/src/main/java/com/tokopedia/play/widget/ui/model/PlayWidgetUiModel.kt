package com.tokopedia.play.widget.ui.model

import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by mzennis on 05/10/20.
 */
sealed class PlayWidgetUiModel {

    data class Small(
            val title: String,
            val actionTitle: String,
            val actionAppLink: String,
            val actionWebLink: String,
            override val config: PlayWidgetConfigUiModel,
            val useHeader: Boolean,
            val items: List<PlayWidgetSmallItemUiModel>
    ) : PlayWidgetUiModel(), PlayWidgetConfigProvider, ImpressionableModel {

        override val impressHolder = ImpressHolder()
    }

    data class Medium(
            val title: String,
            val actionTitle: String,
            val actionAppLink: String,
            val actionWebLink: String,
            override val config: PlayWidgetConfigUiModel,
            val background: PlayWidgetBackgroundUiModel,
            val items: List<PlayWidgetMediumItemUiModel>
    ) : PlayWidgetUiModel(), PlayWidgetConfigProvider, ImpressionableModel {

        override val impressHolder = ImpressHolder()
    }

    object Placeholder : PlayWidgetUiModel()
}

//data class PlayWidgetSmallUiModel(
//        val title: String,
//        val actionTitle: String,
//        val actionAppLink: String,
//        val actionWebLink: String,
//        val config: PlayWidgetConfigUiModel,
//        val useHeader: Boolean,
//        val items: List<PlayWidgetSmallItemUiModel>
//) : PlayWidgetUiModel()
//
//data class PlayWidgetMediumUiModel(
//        val title: String,
//        val actionTitle: String,
//        val actionAppLink: String,
//        val actionWebLink: String,
//        val config: PlayWidgetConfigUiModel,
//        val background: PlayWidgetBackgroundUiModel,
//        val items: List<PlayWidgetMediumItemUiModel>
//) : PlayWidgetUiModel()