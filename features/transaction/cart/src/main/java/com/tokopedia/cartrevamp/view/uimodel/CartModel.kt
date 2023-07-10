package com.tokopedia.cartrevamp.view.uimodel

import com.tokopedia.cart.domain.model.cartlist.SummaryTransactionUiModel
import com.tokopedia.cart.view.uimodel.PromoSummaryData

data class CartModel(
    var promoSummaryUiModel: PromoSummaryData? = null,
    var summaryTransactionUiModel: SummaryTransactionUiModel? = null
)
