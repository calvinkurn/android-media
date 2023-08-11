package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
open class FeedBrowseUiModel(
    val id: String = "", // @TrackingField
) {
    data class Placeholder(
        val type: String
    ): FeedBrowseUiModel()

    data class Cards(
        val title: String,
        val chips: List<FeedBrowseChipUiModel>,
        val model: PlayWidgetUiModel,
    ): FeedBrowseUiModel()
}


