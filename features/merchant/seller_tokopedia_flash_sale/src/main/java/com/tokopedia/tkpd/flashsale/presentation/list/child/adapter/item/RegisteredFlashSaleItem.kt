package com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import java.util.Date

data class RegisteredFlashSaleItem(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val startDate: Date,
    val endDate: Date,
    val formattedStartDate: String,
    val formattedEndDate: String,
    val reviewStartDate: Date,
    val reviewEndDate: Date,
    val status: FlashSaleStatus,
    val statusText: String,
    val useMultiLocation: Boolean
) : DelegateAdapterItem {
    override fun id() = id
}
