package com.tokopedia.seller.action.balance.presentation.model

import com.tokopedia.seller.action.common.presentation.model.SellerSuccessItem

data class SellerActionBalance (
        val accountBalance: String?,
        val topAdsBalance: String?
): SellerSuccessItem