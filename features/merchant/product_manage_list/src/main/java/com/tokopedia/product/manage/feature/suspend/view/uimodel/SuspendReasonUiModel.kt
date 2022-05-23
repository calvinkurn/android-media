package com.tokopedia.product.manage.feature.suspend.view.uimodel

data class SuspendReasonUiModel(
    val productID: String,
    val title: String = "Pelanggaran produk",
    val infoImpact: String,
    val infoToPrevent: String,
    val infoReason: String,
    val infoToResolve: List<String>,
    val infoFootNote: String,
    val buttonText: String = "Bantuan",
    val buttonApplink: String,
)