package com.tokopedia.bmsm_widget.presentation.model

import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * Created by @ilhamsuaib on 04/12/23.
 */

data class ProductGiftUiModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val qty: Int,
    val isUnlocked: Boolean,
    val tierId: Long = 0L
) {
    fun getProductNameWithQty() = if (qty > Int.ZERO) {
        "${qty}x $name"
    } else {
        name
    }
}