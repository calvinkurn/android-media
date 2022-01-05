package com.tokopedia.play.view.uimodel.recom

import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType

data class PlayStatusUiModel(
    val channelStatus: PlayChannelStatus,
    val config: PlayStatusConfig,
) {

    companion object {
        val Empty: PlayStatusUiModel
            get() = PlayStatusUiModel(
                channelStatus = PlayChannelStatus.Empty,
                config = PlayStatusConfig.Empty,
            )
    }
}

data class PlayStatusConfig(
    val bannedModel: PlayBannedUiModel,
    val freezeModel: FreezeUiModel,
) {
    companion object {
        val Empty: PlayStatusConfig
            get() = PlayStatusConfig(
                bannedModel = PlayBannedUiModel.Empty,
                freezeModel = FreezeUiModel.Empty,
            )
    }
}

data class PlayChannelStatus(
    val statusType: PlayStatusType,
    val statusSource: PlayStatusSource,
    val waitingDuration: Int,
) {
    companion object {
        val Empty: PlayChannelStatus
            get() = PlayChannelStatus(
                statusType = PlayStatusType.Active,
                statusSource = PlayStatusSource.Network,
                waitingDuration = 0,
            )
    }
}

data class FreezeUiModel(
    val title: String,
) {
    companion object {
        val Empty: FreezeUiModel
            get() = FreezeUiModel(title = "")
    }
}

enum class PlayStatusSource {
    Network,
    Socket,
}