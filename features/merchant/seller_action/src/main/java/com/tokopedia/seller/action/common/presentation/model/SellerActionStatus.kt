package com.tokopedia.seller.action.common.presentation.model

sealed class SellerActionStatus {
    class Success(val itemList: List<SellerSuccessItem>): SellerActionStatus()
    object Fail: SellerActionStatus()
    object Loading: SellerActionStatus()
    object NotLogin: SellerActionStatus()
}