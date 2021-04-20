package com.tokopedia.play.widget.ui.model.ext

import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * Created by jegul on 05/11/20
 */
private val watchableChannelTypes = listOf(PlayWidgetChannelType.Live, PlayWidgetChannelType.Vod)

val PlayWidgetUiModel.Medium.hasSuccessfulTranscodedChannel: Boolean
    get() = items.any {
        it is PlayWidgetMediumChannelUiModel &&
                it.channelTypeTransition.prevType == PlayWidgetChannelType.Transcoding &&
                it.channelTypeTransition.currentType in watchableChannelTypes
    }

val PlayWidgetUiModel.Medium.hasFailedTranscodedChannel: Boolean
    get() = items.any {
        it is PlayWidgetMediumChannelUiModel &&
                it.channelTypeTransition.prevType == PlayWidgetChannelType.Transcoding &&
                it.channelTypeTransition.currentType == PlayWidgetChannelType.FailedTranscoding
    }