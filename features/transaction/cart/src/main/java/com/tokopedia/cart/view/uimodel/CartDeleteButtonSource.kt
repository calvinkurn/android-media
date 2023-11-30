package com.tokopedia.cart.view.uimodel

enum class CartDeleteButtonSource(val eventLabel: String) {
    TrashBin("trashbutton"),
    SwipeToDelete("swipetodelete"),
    QuantityEditorImeAction("zeroquantity")
}
