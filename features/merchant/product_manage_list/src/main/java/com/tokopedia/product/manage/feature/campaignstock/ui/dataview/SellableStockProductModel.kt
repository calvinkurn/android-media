package com.tokopedia.product.manage.feature.campaignstock.ui.dataview

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SellableStockProductModel(
        val productId: String,
        val warehouseId: String,
        val productName: String,
        val stock: String): Parcelable