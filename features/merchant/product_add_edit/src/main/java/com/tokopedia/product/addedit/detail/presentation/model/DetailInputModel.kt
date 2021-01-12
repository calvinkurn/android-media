package com.tokopedia.product.addedit.detail.presentation.model

import android.os.Parcelable
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.CONDITION_NEW
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.DEFAULT_MIN_ORDER_VALUE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.DEFAULT_STOCK_VALUE
import com.tokopedia.product.addedit.specification.presentation.model.SpecificationInputModel
import com.tokopedia.shop.common.data.model.ShowcaseItemPicker
import kotlinx.android.parcel.Parcelize
import java.math.BigInteger

/**
 * Created by faisalramd on 2020-03-22.
 */

@Parcelize
data class DetailInputModel(
        var productName: String = "",
        var currentProductName: String = "", // product name, before do editing
        var categoryName: String = "",
        var categoryId: String = "",
        var catalogId: String = "",
        var price: BigInteger = 0.toBigInteger(),
        var stock: Int = DEFAULT_STOCK_VALUE,
        var minOrder: Int = DEFAULT_MIN_ORDER_VALUE,
        var condition: String = CONDITION_NEW,
        var sku: String = "",
        var status: Int = 1,
        var imageUrlOrPathList: List<String> = emptyList(),
        var preorder: PreorderInputModel = PreorderInputModel(),
        var wholesaleList: List<WholeSaleInputModel> = emptyList(),
        var pictureList: List<PictureInputModel> = emptyList(),
        var productShowCases: List<ShowcaseItemPicker> = emptyList(),
        var specifications: List<SpecificationInputModel>? = null
) : Parcelable

@Parcelize
data class PreorderInputModel(
        var duration: Int = 0,
        var timeUnit: Int = 0,
        var isActive: Boolean = false
) : Parcelable