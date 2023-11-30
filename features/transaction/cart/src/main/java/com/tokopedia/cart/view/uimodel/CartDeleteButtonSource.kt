package com.tokopedia.cart.view.uimodel

sealed interface CartDeleteButtonSource {

    object TrashBin : CartDeleteButtonSource
    object SwipeToDelete : CartDeleteButtonSource
    object QuantityEditor : CartDeleteButtonSource
}
