package com.tokopedia.product_ar.model

data class ModifaceUiModel(
        val modifaceProvider: ModifaceProvider = ModifaceProvider(),
        val isSelected: Boolean = false,
        val backgroundUrl: String = "",
        val productName: String = "",
        val productId: String = ""
)
