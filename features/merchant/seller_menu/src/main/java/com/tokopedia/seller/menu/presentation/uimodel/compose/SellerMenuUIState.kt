package com.tokopedia.seller.menu.presentation.uimodel.compose

sealed class SellerMenuUIState {
    data class OnSuccessGetMenuList(
        val visitableList: List<SellerMenuComposeItem>,
        val isInitialValue: Boolean = false
    ) : SellerMenuUIState()
    data class OnFailedGetMenuList(
        val throwable: Throwable,
        val visitableList: List<SellerMenuComposeItem>
    ) : SellerMenuUIState()
    object Idle : SellerMenuUIState()
}
