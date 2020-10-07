package com.tokopedia.play.widget

import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetCardUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetCardSize


/**
 * Created by mzennis on 05/10/20.
 */
data class PlayWidgetUiModel(
        val title: String,
        val actionTitle: String,
        val actionAppLink: String,
        val actionWebLink: String,
        val background: PlayWidgetBackgroundUiModel,
        val config: PlayWidgetConfigUiModel,
        val items: List<PlayWidgetCardUiModel>,
        val size: PlayWidgetCardSize = PlayWidgetCardSize.Small
)