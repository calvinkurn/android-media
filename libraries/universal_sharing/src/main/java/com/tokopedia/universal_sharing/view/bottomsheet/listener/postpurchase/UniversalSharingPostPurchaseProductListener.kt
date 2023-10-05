package com.tokopedia.universal_sharing.view.bottomsheet.listener.postpurchase

interface UniversalSharingPostPurchaseProductListener {
    fun onClickShare(orderId: String, shopName: String, productId: String)
}
