package com.tokopedia.seller.menu.presentation.uimodel.compose

sealed class SellerMenuUIState(
    val notification: Int? = null
) {
    data class OnSuccessGetMenuList(
        val visitableList: List<SellerMenuComposeItem>,
        val isInitialValue: Boolean = false,
        val notifications: Int? = null
    ): SellerMenuUIState(notifications)
    data class OnFailedGetMenuList(
        val throwable: Throwable,
        val notifications: Int? = null
    ): SellerMenuUIState(notifications)
    object Idle: SellerMenuUIState()

}
