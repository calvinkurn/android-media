package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.feedplus.browse.presentation.view.FeedBrowsePlaceholderView
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
open class FeedBrowseUiModel(
    val id: String = "", // @TrackingField
) {
    data class Placeholder(
        val type: FeedBrowsePlaceholderView.Type
    ): FeedBrowseUiModel()

    data class Cards(
        val title: String,
        val model: PlayWidgetUiModel
    ): FeedBrowseUiModel()

    data class Chips(
        val title: String,
        val chips: Map<FeedBrowseChipUiModel, List<Cards>>
    ): FeedBrowseUiModel()
}
