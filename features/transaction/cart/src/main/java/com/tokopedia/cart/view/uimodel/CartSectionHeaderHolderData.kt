package com.tokopedia.cart.view.uimodel

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

data class CartSectionHeaderHolderData(
    var title: String = "",
    var showAllAppLink: String = "",
    var type: CartSectionHeaderActionType = CartSectionHeaderActionType.TEXT_BUTTON
)

enum class CartSectionHeaderActionType {
    TEXT_BUTTON,
    ICON_BUTTON
}
