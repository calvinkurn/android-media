package com.tokopedia.play.view.uimodel.recom

/**
 * Created by jegul on 01/02/21
 */
data class PlayStatusInfoUiModel(
        val isBanned: Boolean,
        val isFreeze: Boolean,
        val bannedModel: PlayBannedUiModel,
        val freezeModel: PlayFreezeUiModel
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