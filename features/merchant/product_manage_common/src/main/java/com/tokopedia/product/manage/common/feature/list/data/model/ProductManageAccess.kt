package com.tokopedia.product.manage.common.feature.list.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductManageAccess(
    val addProduct: Boolean,
    val editProduct: Boolean,
    val etalaseList: Boolean,
    val multiSelect: Boolean,
    val editPrice: Boolean,
    val editStock: Boolean,
    val updateStock: Boolean,
    val duplicateProduct: Boolean,
    val setStockReminder: Boolean,
    val deleteProduct: Boolean,
    val setTopAds: Boolean,
    val setCashBack: Boolean,
    val setFeatured: Boolean,
    val productList: Boolean
): Parcelable