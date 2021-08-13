package com.tokopedia.play.view.uimodel.recom

import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType

/**
 * Created by jegul on 01/02/21
 */
data class PlayStatusInfoUiModel(
        val statusType: PlayStatusType,
        val bannedModel: PlayBannedUiModel,
        val freezeModel: PlayFreezeUiModel,
        val shouldAutoSwipeOnFreeze: Boolean,
)

data class PlayBannedUiModel(
        val title: String,
        val message: String,
        val btnTitle: String
)

data class PlayFreezeUiModel(
        val title: String,
        val message: String,
        val btnTitle: String,
        val btnUrl: String
)