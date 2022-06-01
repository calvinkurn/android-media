package com.tokopedia.play.widget.ui.model

/**
 * Created by meyta.taliti on 31/01/22.
 */
sealed class PlayFeedUiModel

data class PlayWidgetJumboUiModel(
    val model: PlayWidgetUiModel
): PlayFeedUiModel()

data class PlayWidgetLargeUiModel(
    val model: PlayWidgetUiModel
): PlayFeedUiModel()

data class PlayWidgetMediumUiModel(
    val model: PlayWidgetUiModel
): PlayFeedUiModel()

data class PlayWidgetSlotTabUiModel(
    val labels: List<Pair<String, Boolean>>
): PlayFeedUiModel()