package com.tokopedia.cart.view.uimodel

data class CartDeleteItemData(
    val removedCartItems: List<CartItemHolderData> = emptyList(),
    val addWishList: Boolean = false,
    val forceExpandCollapsedUnavailableItems: Boolean = false,
    val isFromGlobalCheckbox: Boolean = false,
    val isFromEditBundle: Boolean = false,
    val listCartStringOrderAndBmGmOfferId: ArrayList<String> = arrayListOf(),
    val deleteSource: CartDeleteButtonSource = CartDeleteButtonSource.TrashBin
)
