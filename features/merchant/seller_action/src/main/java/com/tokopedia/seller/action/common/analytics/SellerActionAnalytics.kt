package com.tokopedia.seller.action.common.analytics

import com.tokopedia.seller.action.common.presentation.model.SellerActionStatus

interface SellerActionAnalytics {

    fun sendSellerActionImpression(status: SellerActionStatus, date: String? = null)
    fun clickOrderAppButton()
    fun clickOrderLine()

}