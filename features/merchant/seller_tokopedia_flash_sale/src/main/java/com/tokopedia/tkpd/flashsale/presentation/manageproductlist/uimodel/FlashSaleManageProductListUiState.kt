package com.tokopedia.tkpd.flashsale.presentation.manageproductlist.uimodel

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.entity.RemoteTicker

data class FlashSaleManageProductListUiState(
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val listDelegateItem: List<DelegateAdapterItem> = listOf(),
    val currentPage: Int = 0,
    val totalProduct: Int = 0,
    val showTicker: Boolean = false,
    val tickerList: List<RemoteTicker> = emptyList()
)
