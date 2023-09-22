package com.tokopedia.universal_sharing.view.uistate

import com.tokopedia.universal_sharing.data.model.UniversalSharingPostPurchaseProductResponse

data class UniversalInboxPostPurchaseSharingUiState(
    val productId: String = "",
    val productData: UniversalSharingPostPurchaseProductResponse? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)
