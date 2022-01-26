package com.tokopedia.play.model.status

import com.tokopedia.play.view.uimodel.recom.BannedUiModel
import com.tokopedia.play.view.uimodel.recom.FreezeUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelStatus
import com.tokopedia.play.view.uimodel.recom.PlayStatusConfig
import com.tokopedia.play.view.uimodel.recom.PlayStatusSource
import com.tokopedia.play.view.uimodel.recom.PlayStatusUiModel
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType

class ChannelStatusBuilderImpl : ChannelStatusBuilder {

    override fun buildStatus(
        channelStatus: PlayChannelStatus,
        config: PlayStatusConfig,
    ) = PlayStatusUiModel(
        channelStatus = channelStatus,
        config = config,
    )

    override fun buildChannelStatus(
        statusType: PlayStatusType,
        statusSource: PlayStatusSource,
        waitingDuration: Int,
    ) = PlayChannelStatus(
        statusType = statusType,
        statusSource = statusSource,
        waitingDuration = waitingDuration,
    )

    override fun buildStatusConfig(
        bannedModel: BannedUiModel,
        freezeModel: FreezeUiModel,
    ) = PlayStatusConfig(
        bannedModel = bannedModel,
        freezeModel = freezeModel,
    )

    override fun buildBannedModel(
        title: String,
        message: String,
        btnTitle: String
    ) = BannedUiModel(
        title = title,
        message = message,
        btnTitle = btnTitle,
    )

    override fun buildFreezeModel(
        title: String
    ) = FreezeUiModel(
        title = title,
    )
}