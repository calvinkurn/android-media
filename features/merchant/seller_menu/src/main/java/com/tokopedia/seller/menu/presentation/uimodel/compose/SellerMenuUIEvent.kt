package com.tokopedia.seller.menu.presentation.uimodel.compose

sealed class SellerMenuUIEvent {

    object GetInitialMenu: SellerMenuUIEvent()
    object Idle: SellerMenuUIEvent()
    data class OnSuccessGetShopInfoUse(val shopAge: Long, val isNewSeller: Boolean): SellerMenuUIEvent()

}
