package com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import java.util.Date

data class OngoingFlashSaleItem(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val formattedStartDate: String,
    val formattedEndDate: String,
    val endDate: Date,
    val totalStockSold: Int,
    val totalProductStock: Int,
    val status: FlashSaleStatus,
    val statusText: String,
    val useMultiLocation: Boolean
) : DelegateAdapterItem {
    override fun id() = id
}
