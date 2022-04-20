package com.tokopedia.product.manage.feature.suspend.view.uimodel

import com.google.gson.annotations.SerializedName

data class SuspendReasonUiModel(
    val productID: String,
    val title:String="Palanggran produk",
    val infoImpact: String,
    val infoToPrevent: String,
    val infoReason: String,
    val infoToResolve: List<String>,
    val infoFootNote: String,
    val buttonText: String="Bantuan",
    val buttonApplink: String,
)