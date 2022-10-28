package com.tokopedia.play.view.uimodel.recom

import com.tokopedia.play.view.uimodel.recom.realtimenotif.PlayRealTimeNotificationConfig

/**
 * Created by jegul on 23/07/21
 */
data class PlayChannelDetailUiModel(
        val shareInfo: PlayShareInfoUiModel = PlayShareInfoUiModel(),
        val channelInfo: PlayChannelInfoUiModel = PlayChannelInfoUiModel(),
        val rtnConfigInfo: PlayRealTimeNotificationConfig = PlayRealTimeNotificationConfig(),
        val videoInfo: PlayVideoConfigUiModel = PlayVideoConfigUiModel(),
        val emptyBottomSheetInfo: PlayEmptyBottomSheetInfoUiModel = PlayEmptyBottomSheetInfoUiModel(),
        val bottomSheetTitle: String = "",
)