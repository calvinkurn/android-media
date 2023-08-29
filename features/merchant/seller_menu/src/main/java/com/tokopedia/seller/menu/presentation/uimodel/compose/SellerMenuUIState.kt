package com.tokopedia.seller.menu.presentation.uimodel.compose

sealed class SellerMenuUIState {

    data class OnSuccessGetMenuList(val visitableList: List<SellerMenuComposeItem>): SellerMenuUIState()
    data class OnFailedGetMenuList(val throwable: Throwable): SellerMenuUIState()
    object Idle: SellerMenuUIState()

}
