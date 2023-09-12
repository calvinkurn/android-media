package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel

/**
 * Created by meyta.taliti on 06/09/23.
 */
data class FeedBrowseConfigUiModel(
    val data: PlayWidgetConfigUiModel,
    val lastUpdated: Long
)
