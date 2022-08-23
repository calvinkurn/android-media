package com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem

data class OngoingFlashSaleItem(val id: Long, val name : String) : DelegateAdapterItem {
    override fun id() = id
}
