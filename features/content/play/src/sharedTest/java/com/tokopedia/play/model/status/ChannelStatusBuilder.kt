package com.tokopedia.play.model.status

import com.tokopedia.play.view.uimodel.recom.BannedUiModel
import com.tokopedia.play.view.uimodel.recom.FreezeUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelStatus
import com.tokopedia.play.view.uimodel.recom.PlayStatusConfig
import com.tokopedia.play.view.uimodel.recom.PlayStatusSource
import com.tokopedia.play.view.uimodel.recom.PlayStatusUiModel
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.model.ui.ArchivedUiModel

/**
 * Created by kenny.hadisaputra on 11/05/22
 */
interface ChannelStatusBuilder {

    fun buildStatus(
        channelStatus: PlayChannelStatus = buildChannelStatus(),
        config: PlayStatusConfig = buildStatusConfig()
    ): PlayStatusUiModel

    fun buildChannelStatus(
        statusType: PlayStatusType = PlayStatusType.Active,
        statusSource: PlayStatusSource = PlayStatusSource.Network,
        waitingDuration: Int = 0
    ): PlayChannelStatus

    fun buildStatusConfig(
        bannedModel: BannedUiModel = buildBannedModel(),
        freezeModel: FreezeUiModel = buildFreezeModel(),
        archiveModel: ArchivedUiModel = buildArchiveModel(),
        ): PlayStatusConfig

    fun buildBannedModel(
        title: String = "Ini banned",
        message: String = "",
        btnTitle: String = "",
    ): BannedUiModel

    fun buildFreezeModel(
        title: String = "Ini freeze",
    ): FreezeUiModel

    fun buildArchiveModel(
        title: String = "Archive",
        description: String = "Channel ini gak ada",
        appLink: String = "tokopedia://play",
        btnTitle: String = "Kembali",
    ): ArchivedUiModel
}
