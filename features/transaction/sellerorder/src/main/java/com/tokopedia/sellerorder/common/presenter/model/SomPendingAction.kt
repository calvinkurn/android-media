package com.tokopedia.sellerorder.common.presenter.model

data class SomPendingAction(
        val actionName: String,
        val orderId: String,
        val action: () -> Unit
)