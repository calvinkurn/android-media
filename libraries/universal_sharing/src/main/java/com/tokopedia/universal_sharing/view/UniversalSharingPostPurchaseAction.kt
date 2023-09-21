package com.tokopedia.universal_sharing.view

import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseModel

sealed class UniversalSharingPostPurchaseAction {
    data class RefreshData(
        val data: UniversalSharingPostPurchaseModel
    ) : UniversalSharingPostPurchaseAction()
    data class ClickShare(val productId: String) : UniversalSharingPostPurchaseAction()
}
