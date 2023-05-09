package com.tokopedia.product.manage.feature.stockreminder.view.data

data class ProductStockReminderUiModel(
    val id: String,
    val productParentName: String,
    val productName: String,
    var stockAlertCount: Int,
    var stockAlertStatus: Int,
    val stock: Int,
    val variantFirst: String = "",
    val variantSecond: String = "",
    var haveChanges: Boolean = false,
    val maxStock: Int? = null
)
