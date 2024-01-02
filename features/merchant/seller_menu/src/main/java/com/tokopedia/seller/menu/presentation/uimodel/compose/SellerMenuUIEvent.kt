package com.tokopedia.seller.menu.presentation.uimodel.compose

sealed class SellerMenuUIEvent {

    object GetInitialMenu : SellerMenuUIEvent()
    object GetShopInfo : SellerMenuUIEvent()
    object OnRefresh : SellerMenuUIEvent()
    object Idle : SellerMenuUIEvent()
    data class OnSuccessGetShopInfo(val shopAge: Long, val isNewSeller: Boolean) : SellerMenuUIEvent()
}
