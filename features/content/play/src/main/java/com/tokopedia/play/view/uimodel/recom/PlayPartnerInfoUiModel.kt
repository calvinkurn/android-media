package com.tokopedia.play.view.uimodel.recom

import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.type.PartnerFolowStatus

/**
 * Created by jegul on 21/01/21
 */
data class PlayPartnerInfoUiModel(
        val id: Long,
        val name: String,
        val type: PartnerType,
        var followStatus: PartnerFolowStatus,
        val isFollowable: Boolean
)

var PlayPartnerInfoUiModel.isFollowed: Boolean
    get() = followStatus == PartnerFolowStatus.Followed
    set(value) {
        followStatus = if (value) PartnerFolowStatus.Followed
        else PartnerFolowStatus.NotFollowed
    }