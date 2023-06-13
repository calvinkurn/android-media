package com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleCategory

sealed class FlashSaleListUiEffect {
    data class FetchCategoryError(val throwable: Throwable) : FlashSaleListUiEffect()
    data class FetchFlashSaleError(val throwable: Throwable) : FlashSaleListUiEffect()
    data class ShowSortBottomSheet(val selectedSortId: String) : FlashSaleListUiEffect()
    data class ShowCategoryBottomSheet(val selectedCategoryIds: List<Long>, val categories: List<FlashSaleCategory>) : FlashSaleListUiEffect()
    data class ShowStatusBottomSheet(val selectedStatusIds: List<String>) : FlashSaleListUiEffect()
    data class LoadNextPageSuccess(val totalItems: Int, val allItems: List<DelegateAdapterItem>, val currentPageItems: List<DelegateAdapterItem>) : FlashSaleListUiEffect()
}
