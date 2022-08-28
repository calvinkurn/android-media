package com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel

import com.tokopedia.campaign.entity.MultipleSelectionItem
import com.tokopedia.campaign.entity.SingleSelectionItem

sealed class FlashSaleListUiEvent {
    data class Init(val tabName: String, val tabId: Int) : FlashSaleListUiEvent()
    data class LoadPage(val offset: Int) : FlashSaleListUiEvent()
    object ChangeSort : FlashSaleListUiEvent()
    data class ApplySort(val selectedSort: SingleSelectionItem) : FlashSaleListUiEvent()
    object ChangeCategory : FlashSaleListUiEvent()
    data class ApplyCategoryFilter(val categories: List<MultipleSelectionItem>) : FlashSaleListUiEvent()
    object ChangeStatus : FlashSaleListUiEvent()
    data class ApplyStatusFilter(val statuses: List<MultipleSelectionItem>) : FlashSaleListUiEvent()
    object ClearFilter : FlashSaleListUiEvent()
}