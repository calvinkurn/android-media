package com.tokopedia.play.domain.repository

import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction

/**
 * Created by jegul on 05/07/21
 */
interface PlayViewerPartnerRepository {

    suspend fun getIsFollowingPartner(partnerId: Long): Boolean

    suspend fun postFollowStatus(shopId: String, followAction: PartnerFollowAction): Boolean
}