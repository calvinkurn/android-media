package com.tokopedia.product.addedit.detail.presentation.model

import android.os.Parcelable
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CONDITION_NEW
import kotlinx.android.parcel.Parcelize

/**
 * Created by faisalramd on 2020-03-22.
 */

@Parcelize
data class DetailInputModel(
        var productName: String = "",
        var categoryName: String = "",
        var categoryId: String = "",
        var catalogId: String = "",
        var price: Long = 0L,
        var stock: Int = 0,
        var minOrder: Int = 0,
        var condition: String = CONDITION_NEW,
        var sku: String = "",
        var imageUrlOrPathList: List<String> = emptyList(),
        var preorder: PreorderInputModel = PreorderInputModel(),
        var wholesaleList: List<WholeSaleInputModel> = emptyList(),
        var pictureList: List<PictureInputModel> = emptyList()
) : Parcelable

@Parcelize
data class PreorderInputModel(
        var duration: Int = 0,
        var timeUnit: Int = 0,
        var isActive: Boolean = false
) : Parcelable