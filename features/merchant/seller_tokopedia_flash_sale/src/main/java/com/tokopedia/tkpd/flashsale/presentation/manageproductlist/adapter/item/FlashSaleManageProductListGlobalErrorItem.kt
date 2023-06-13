package com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.item

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem

data class FlashSaleManageProductListGlobalErrorItem(
    val throwable: Throwable
) : DelegateAdapterItem {
    override fun id() = true
}

