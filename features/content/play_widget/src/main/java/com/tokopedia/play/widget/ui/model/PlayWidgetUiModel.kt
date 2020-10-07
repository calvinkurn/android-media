package com.tokopedia.play.widget.ui.model

/**
 * Created by mzennis on 05/10/20.
 */
sealed class PlayWidgetUiModel {

    abstract val title: String
    abstract val actionTitle: String
    abstract val actionAppLink: String
    abstract val actionWebLink: String

    abstract val config: PlayWidgetConfigUiModel
}

data class PlayWidgetSmallUiModel(
        override val title: String,
        override val actionTitle: String,
        override val actionAppLink: String,
        override val actionWebLink: String,
        override val config: PlayWidgetConfigUiModel,
        val useHeader: Boolean,
        val items: List<PlayWidgetSmallItemUiModel>
) : PlayWidgetUiModel()

data class PlayWidgetMediumUiModel(
        override val title: String,
        override val actionTitle: String,
        override val actionAppLink: String,
        override val actionWebLink: String,
        override val config: PlayWidgetConfigUiModel,
        val background: PlayWidgetBackgroundUiModel,
        val items: List<PlayWidgetMediumItemUiModel>
) : PlayWidgetUiModel()