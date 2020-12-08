package com.tokopedia.play.widget.ui.model

import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * Created by jegul on 05/11/20
 */
data class PlayWidgetChannelTypeTransition(
        val prevType: PlayWidgetChannelType?,
        val currentType: PlayWidgetChannelType
)

fun PlayWidgetChannelTypeTransition.changeTo(newType: PlayWidgetChannelType) = PlayWidgetChannelTypeTransition(currentType, newType)