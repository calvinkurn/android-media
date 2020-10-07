package com.tokopedia.play.widget.ui.model

import com.tokopedia.play.widget.ui.type.PlayWidgetCardType


/**
 * Created by mzennis on 07/10/20.
 */
data class PlayWidgetCardUiModel(
        val type: PlayWidgetCardType,
        val card: PlayWidgetCardItemUiModel
)