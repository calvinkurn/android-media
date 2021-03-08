package com.tokopedia.play.model

import com.tokopedia.play.view.uimodel.recom.PlayBannedUiModel
import com.tokopedia.play.view.uimodel.recom.PlayFreezeUiModel
import com.tokopedia.play.view.uimodel.recom.PlayStatusInfoUiModel
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType

/**
 * Created by jegul on 09/02/21
 */
class PlayStatusInfoModelBuilder {

    fun build(
            statusType: PlayStatusType = PlayStatusType.Active,
            bannedModel: PlayBannedUiModel = buildBannedModel(),
            freezeModel: PlayFreezeUiModel = buildFreezeModel(),
            shouldAutoSwipeOnFreeze: Boolean = true
    ) = PlayStatusInfoUiModel(
            statusType = statusType,
            bannedModel = bannedModel,
            freezeModel = freezeModel,
            shouldAutoSwipeOnFreeze = shouldAutoSwipeOnFreeze
    )

    fun buildBannedModel(
            title: String = "Anda di banned",
            message: String = "Anda telah melanggar peraturan",
            btnTitle: String = "Keluar"
    ) = PlayBannedUiModel(
            title = title,
            message = message,
            btnTitle = btnTitle
    )

    fun buildFreezeModel(
            title: String = "Channel telah berakhir",
            message: String = "Channel ini telah selesai Live",
            btnTitle: String = "Lihat channel lainnya",
            btnUrl: String = "https://www.tokopedia.com"
    ) = PlayFreezeUiModel(
            title = title,
            message = message,
            btnTitle = btnTitle,
            btnUrl = btnUrl
    )
}