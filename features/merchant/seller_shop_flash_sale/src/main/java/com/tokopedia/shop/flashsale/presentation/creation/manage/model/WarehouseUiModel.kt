package com.tokopedia.shop.flashsale.presentation.creation.manage.model

data class WarehouseUiModel (
    val id: String = "",
    val name: String = "",
    val stock: Long = 0,
    var isSelected: Boolean = false
)