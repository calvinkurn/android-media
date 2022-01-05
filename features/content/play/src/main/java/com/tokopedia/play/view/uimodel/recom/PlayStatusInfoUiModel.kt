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
        val waitingDuration: Int,
)

data class PlayBannedUiModel(
        val title: String,
        val message: String,
        val btnTitle: String
) {
        companion object {
                val Empty: PlayBannedUiModel
                        get() = PlayBannedUiModel(
                                title = "",
                                message = "",
                                btnTitle = "",
                        )
        }
}

data class PlayFreezeUiModel(
        val title: String,
        val message: String,
        val btnTitle: String,
        val btnUrl: String
)