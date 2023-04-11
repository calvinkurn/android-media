package com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus

data class FinishedFlashSaleItem(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val formattedStartDate: String,
    val formattedEndDate: String,
    val status: FlashSaleStatus,
    val statusText: String,
    val productMeta: FlashSale.ProductMeta,
    val cancellationReason: String,
    val productSoldPercentage: Int,
    val useMultiLocation: Boolean
) : DelegateAdapterItem {
    override fun id() = id
}
