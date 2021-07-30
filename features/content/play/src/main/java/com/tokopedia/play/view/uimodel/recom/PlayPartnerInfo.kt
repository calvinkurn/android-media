package com.tokopedia.play.view.uimodel.recom

import com.tokopedia.play.ui.toolbar.model.PartnerType

/**
 * Created by jegul on 21/01/21
 */
data class PlayPartnerInfo(
        val id: Long = 0L,
        val name: String = "",
        val type: PartnerType = PartnerType.Unknown,
        val status: PlayPartnerFollowStatus = PlayPartnerFollowStatus.Unknown,
)

sealed class PlayPartnerFollowStatus {

    data class Followable(val isFollowing: Boolean) : PlayPartnerFollowStatus()
    object NotFollowable : PlayPartnerFollowStatus()
    object Unknown : PlayPartnerFollowStatus()
}