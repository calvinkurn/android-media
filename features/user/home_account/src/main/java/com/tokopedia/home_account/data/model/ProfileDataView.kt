package com.tokopedia.home_account.data.model

import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusDataModel

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class ProfileDataView(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val avatar: String = "",
    var isLinked: Boolean = false,
    var isShowLinkStatus: Boolean = false,
    var memberStatus: TierData = TierData(),
    var isSuccessGetTokopediaPlusData: Boolean = false,
    var tokopediaPlusWidget: TokopediaPlusDataModel = TokopediaPlusDataModel(),
    var offerInterruptData: OfferInterruptData = OfferInterruptData()
)
