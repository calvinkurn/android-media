package com.tokopedia.play.view.uimodel.recom

import com.tokopedia.play.ui.toolbar.model.PartnerType

/**
 * Created by jegul on 21/01/21
 */
sealed class PlayPartnerInfoUiModel {

    abstract val basicInfo: PlayPartnerBasicInfoUiModel

    data class Incomplete(override val basicInfo: PlayPartnerBasicInfoUiModel) : PlayPartnerInfoUiModel()
    data class Complete(
            override val basicInfo: PlayPartnerBasicInfoUiModel,
            val followInfo: PlayPartnerFollowInfoUiModel
    ) : PlayPartnerInfoUiModel()
}

data class PlayPartnerBasicInfoUiModel(
        val id: Long,
        val name: String,
        val type: PartnerType,
)

data class PlayPartnerFollowInfoUiModel(
        var isFollowed: Boolean,
        val isFollowable: Boolean
)

var PlayPartnerInfoUiModel.isFollowed: Boolean
    get() {
        return when (this) {
            is PlayPartnerInfoUiModel.Incomplete -> false
            is PlayPartnerInfoUiModel.Complete -> followInfo.isFollowed
        }
    }
    set(value) {
        if (this is PlayPartnerInfoUiModel.Complete) {
            followInfo.isFollowed = value
        }
    }

val PlayPartnerInfoUiModel.isFollowable: Boolean
    get() {
        return when (this) {
            is PlayPartnerInfoUiModel.Incomplete -> false
            is PlayPartnerInfoUiModel.Complete -> followInfo.isFollowable
        }
    }