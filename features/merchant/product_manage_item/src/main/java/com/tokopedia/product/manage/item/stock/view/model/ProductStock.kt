package com.tokopedia.product.manage.item.stock.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductStock(var isActive: Boolean = true, var stockCount: Int = 1, var sku: String = "") : Parcelable