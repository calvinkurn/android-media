package com.tokopedia.play.widget.ui.model

/**
 * Created by mzennis on 05/10/20.
 */
sealed class PlayWidgetUiModel {

    sealed class Small : PlayWidgetUiModel() {

        data class Widget(
                val title: String,
                val actionTitle: String,
                val actionAppLink: String,
                val actionWebLink: String,
                val config: PlayWidgetConfigUiModel,
                val useHeader: Boolean,
                val items: List<PlayWidgetSmallItemUiModel>
        ) : Small()

        object Empty : Small()
    }

    sealed class Medium : PlayWidgetUiModel() {

        data class Widget(
                val title: String,
                val actionTitle: String,
                val actionAppLink: String,
                val actionWebLink: String,
                val config: PlayWidgetConfigUiModel,
                val background: PlayWidgetBackgroundUiModel,
                val items: List<PlayWidgetMediumItemUiModel>
        ) : Medium()

        object Empty : Medium()
    }
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