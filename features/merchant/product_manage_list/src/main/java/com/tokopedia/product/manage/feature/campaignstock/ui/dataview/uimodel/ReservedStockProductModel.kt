package com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReservedStockProductModel (
        val productId: String,
        val warehouseId: String,
        val productName: String,
        val description: String,
        val stock: String
): Parcelable