package com.tokopedia.play.domain.repository

import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowInfo

/**
 * Created by jegul on 05/07/21
 */
interface PlayViewerPartnerRepository {

    suspend fun getPartnerFollowInfo(partnerId: Long): PlayPartnerFollowInfo

    suspend fun postFollowStatus(shopId: String, followAction: PartnerFollowAction): Boolean
}