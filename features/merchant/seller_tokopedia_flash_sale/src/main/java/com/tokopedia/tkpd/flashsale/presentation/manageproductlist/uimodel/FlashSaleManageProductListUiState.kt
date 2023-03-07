package com.tokopedia.tkpd.flashsale.presentation.manageproductlist.uimodel

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem

data class FlashSaleManageProductListUiState(
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val listDelegateItem: List<DelegateAdapterItem> = listOf(),
    val currentPage: Int = 0,
    val totalProduct: Int = 0
)