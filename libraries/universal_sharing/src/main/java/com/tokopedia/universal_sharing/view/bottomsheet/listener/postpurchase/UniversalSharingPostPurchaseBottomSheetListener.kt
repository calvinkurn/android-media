package com.tokopedia.universal_sharing.view.bottomsheet.listener.postpurchase

import com.tokopedia.universal_sharing.data.model.UniversalSharingPostPurchaseProductResponse

interface UniversalSharingPostPurchaseBottomSheetListener {
    fun onOpenShareBottomSheet(
        orderId: String,
        product: UniversalSharingPostPurchaseProductResponse
    )
    fun onDismiss(shouldClosePage: Boolean)
    fun onClickClose()
}
