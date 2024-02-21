package com.tokopedia.cart.view.uimodel

import androidx.compose.runtime.Stable

@Stable
data class CartBuyAgainFloatingButtonData(
    val title: String = "",
    val isVisible: Boolean = false
)
