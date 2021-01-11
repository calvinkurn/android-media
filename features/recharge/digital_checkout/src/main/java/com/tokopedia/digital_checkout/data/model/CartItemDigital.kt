package com.tokopedia.digital_checkout.data.model

/**
 * @author by jessica on 11/01/21
 */

data class CartItemDigital(
        val label: String,
        val value: String,
)

data class CartItemDigitalWithTitle(
        val title: String = "",
        val items: List<CartItemDigital> = listOf()
)