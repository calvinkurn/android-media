package com.tokopedia.play.widget.ui.model.ext

import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * Created by jegul on 05/11/20
 */
private val watchableChannelTypes = listOf(PlayWidgetChannelType.Live, PlayWidgetChannelType.Vod)

val PlayWidgetUiModel.hasSuccessfulTranscodedChannel: Boolean
    get() = items.any {
//        it is PlayWidgetChannelUiModel &&
//                it.channelTypeTransition.prevType == PlayWidgetChannelType.Transcoding &&
//                it.channelTypeTransition.currentType in watchableChannelTypes
        false
    }

val PlayWidgetUiModel.hasFailedTranscodedChannel: Boolean
    get() = items.any {
//        it is PlayWidgetChannelUiModel &&
//                it.channelTypeTransition.prevType == PlayWidgetChannelType.Transcoding &&
//                it.channelTypeTransition.currentType == PlayWidgetChannelType.FailedTranscoding
        false
    }