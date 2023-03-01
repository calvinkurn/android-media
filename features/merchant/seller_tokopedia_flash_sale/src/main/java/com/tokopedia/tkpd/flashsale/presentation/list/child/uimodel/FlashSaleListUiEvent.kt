package com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel

import com.tokopedia.campaign.components.bottomsheet.selection.entity.MultipleSelectionItem
import com.tokopedia.campaign.components.bottomsheet.selection.entity.SingleSelectionItem
import java.util.Date

sealed class FlashSaleListUiEvent {
    data class GetFlashSaleCategory(val tabName: String) : FlashSaleListUiEvent()
    data class LoadPage(val tabId: Int, val tabName: String, val offset: Int, val currentDate: Date) : FlashSaleListUiEvent()
    object ChangeSort : FlashSaleListUiEvent()
    data class ApplySort(val selectedSort: SingleSelectionItem) : FlashSaleListUiEvent()
    object ChangeCategory : FlashSaleListUiEvent()
    data class ApplyCategoryFilter(val categories: List<MultipleSelectionItem>) : FlashSaleListUiEvent()
    object ChangeStatus : FlashSaleListUiEvent()
    data class ApplyStatusFilter(val statuses: List<MultipleSelectionItem>) : FlashSaleListUiEvent()
    object ClearFilter : FlashSaleListUiEvent()
}
