package com.tokopedia.cart.old.view.uimodel

import com.tokopedia.topads.sdk.domain.model.CpmModel

data class CartTopAdsHeadlineData(
        var cpmModel: CpmModel? = null,
        var isHiddenBecauseOfError: Boolean = false
)