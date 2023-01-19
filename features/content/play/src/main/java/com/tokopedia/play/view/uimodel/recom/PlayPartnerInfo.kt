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
    val iconUrl: String = "",
    val badgeUrl: String = "",
    val isLoadingFollow: Boolean = false,
    val appLink: String = "",
)

val PlayPartnerInfo.needFollow: Boolean
    get() =  status is PlayPartnerFollowStatus.Followable && (status as? PlayPartnerFollowStatus.Followable)?.followStatus != PartnerFollowableStatus.Followed

sealed class PlayPartnerFollowStatus {

    data class Followable(val followStatus: PartnerFollowableStatus) : PlayPartnerFollowStatus()
    object NotFollowable : PlayPartnerFollowStatus()
    object Unknown : PlayPartnerFollowStatus()
}

enum class PartnerFollowableStatus{
    Unknown,
    Followed,
    NotFollowed,
}
