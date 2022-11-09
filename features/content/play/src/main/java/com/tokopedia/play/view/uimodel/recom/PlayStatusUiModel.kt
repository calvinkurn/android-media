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
    val bannedModel: BannedUiModel,
    val freezeModel: FreezeUiModel,
    val archivedModel: ArchivedUiModel = ArchivedUiModel.Empty,
) {
    companion object {
        val Empty: PlayStatusConfig
            get() = PlayStatusConfig(
                bannedModel = BannedUiModel.Empty,
                freezeModel = FreezeUiModel.Empty,
            )
    }
}

data class PlayChannelStatus(
    val statusType: PlayStatusType,
    val statusSource: PlayStatusSource,
    val waitingDuration: Int,
    val config: PlayStatusConfig,
) {
    companion object {
        val Empty: PlayChannelStatus
            get() = PlayChannelStatus(
                statusType = PlayStatusType.Active,
                statusSource = PlayStatusSource.Network,
                waitingDuration = 0,
                config = PlayStatusConfig.Empty,
            )
    }
}

data class BannedUiModel(
    val title: String,
    val message: String,
    val btnTitle: String
) {
    companion object {
        val Empty: BannedUiModel
            get() = BannedUiModel(
                title = "",
                message = "",
                btnTitle = "",
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

data class ArchivedUiModel(
    val title: String,
    val description: String,
    val btnTitle: String,
    val appLink: String,
) {
    companion object {
        val Empty: ArchivedUiModel
            get() = ArchivedUiModel(
                title = "",
                description = "",
                btnTitle = "",
                appLink = "",
            )
    }
}

enum class PlayStatusSource {
    Network,
    Socket,
}
