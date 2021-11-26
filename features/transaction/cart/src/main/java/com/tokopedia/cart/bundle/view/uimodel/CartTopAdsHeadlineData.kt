package com.tokopedia.cart.bundle.view.uimodel

import com.tokopedia.topads.sdk.domain.model.CpmModel

data class CartTopAdsHeadlineData(
        var cpmModel: CpmModel? = null,
        var isHiddenBecauseOfError: Boolean = false,
        var cartProductIds: List<String> = emptyList()
)