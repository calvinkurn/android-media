package com.tokopedia.play.model

import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowInfo
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo

/**
 * Created by jegul on 09/02/21
 */
class PlayPartnerInfoModelBuilder {

    fun buildPlayPartnerInfo(
            id: Long = 123L,
            name: String = "haha stag",
            type: PartnerType = PartnerType.Shop
    ) = PlayPartnerInfo(
            id = id,
            name = name,
            type = type
    )

    fun buildPlayPartnerFollowInfo(
            isOwnShop: Boolean = false,
            isFollowing: Boolean = true
    ) = PlayPartnerFollowInfo(
            isOwnShop = isOwnShop,
            isFollowing = isFollowing
    )
}