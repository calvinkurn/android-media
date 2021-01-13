package com.tokopedia.product.manage.common.feature.draft.data.model.detail

import android.os.Parcelable
import com.tokopedia.product.manage.common.draft.data.model.detail.ShowCaseInputModel
import kotlinx.android.parcel.Parcelize
import java.math.BigInteger

/**
 * Created by faisalramd on 2020-03-22.
 */

@Parcelize
data class DetailInputModel(
        var productName: String = "",
        var currentProductName: String = "",
        var categoryId: String = "",
        var catalogId: String = "",
        var price: BigInteger = 0.toBigInteger(),
        var stock: Int = 0,
        var minOrder: Int = 0,
        var condition: String = "NEW",
        var status: Int = 1,
        var sku: String = "",
        var imageUrlOrPathList: List<String> = emptyList(),
        var preorder: PreorderInputModel = PreorderInputModel(),
        var wholesaleList: List<WholeSaleInputModel> = emptyList(),
        var pictureList: List<PictureInputModel> = emptyList(),
        var categoryName: String = "",
        var productShowCases: List<ShowCaseInputModel> = emptyList(),
        var specification: List<SpecificationInputModel>? = null
) : Parcelable

@Parcelize
data class PreorderInputModel(
        var duration: Int = 0,
        var timeUnit: Int = 0,
        var isActive: Boolean = false
) : Parcelable