package com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.components.bottomsheet.selection.entity.SingleSelectionItem
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleCategory
import com.tokopedia.tkpd.flashsale.util.constant.TabConstant
import java.util.Date

data class FlashSaleListUiState(
    val isLoading: Boolean = true,
    val tabName: String = "",
    val tabId: Int = TabConstant.TAB_ID_UPCOMING,
    val offset: Int = 0,
    val selectedSort: SingleSelectionItem = SingleSelectionItem(
        "DEFAULT_VALUE_PLACEHOLDER",
        name = "",
        isSelected = false,
        direction = "ASC"
    ),
    val selectedCategoryIds: List<Long> = emptyList(),
    val flashSaleCategories: List<FlashSaleCategory> = emptyList(),
    val selectedStatusIds: List<String> = emptyList(),
    val isFilterActive: Boolean = false,
    val allItems: List<DelegateAdapterItem> = emptyList(),
    val totalFlashSaleOnCurrentPage: Int = 0,
    val totalFlashSale: Int = 0
)
