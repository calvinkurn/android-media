package com.tokopedia.product.addedit.detail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by faisalramd on 2020-03-22.
 */

@Parcelize
data class DetailInputModel(
        var productName: String = "",
        var categoryId: String = "",
        var catalogId: String = "",
        var price: Float = 0F,
        var stock: Int = 0,
        var minOrder: Int = 0,
        var condition: String = "NEW",
        var sku: String = "",
        var imageUrlOrPathList: List<String> = emptyList(),
        var wholesaleList: List<WholesaleInputModel> = emptyList(),
        var preorderList: List<PreorderInputModel> = emptyList()
) : Parcelable

@Parcelize
data class WholesaleInputModel(
        var minQty: Int = 0,
        var price: Float = 0F
) : Parcelable

@Parcelize
data class PreorderInputModel(
        var duration: Int = 0,
        var timeUnit: Float = 0F
) : Parcelable