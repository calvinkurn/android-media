package com.tokopedia.product.manage.list.data.model

import android.os.Parcelable
import com.tokopedia.product.manage.list.constant.ProductManageListConstant
import com.tokopedia.product.manage.list.constant.option.CatalogProductOption
import com.tokopedia.product.manage.list.constant.option.ConditionProductOption
import com.tokopedia.product.manage.list.constant.option.PictureStatusProductOption
import com.tokopedia.seller.product.manage.constant.ProductManageConstant
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductManageFilterModel(
        var etalaseProductOption: Int = ProductManageListConstant.FILTER_ALL_PRODUK,
        var etalaseProductOptionName: String = "",
        var pictureStatusOption: String = PictureStatusProductOption.WITH_AND_WITHOUT,
        var conditionProductOption: String = ConditionProductOption.ALL_CONDITION,
        var catalogProductOption: String = CatalogProductOption.WITH_AND_WITHOUT,
        var categoryId: String = ProductManageConstant.FILTER_ALL_CATEGORY.toString(),
        var categoryName: String = ""
) : Parcelable {

    fun reset() {
        etalaseProductOption = ProductManageConstant.FILTER_ALL_PRODUK
        etalaseProductOptionName = ""
        categoryId = ProductManageConstant.FILTER_ALL_CATEGORY.toString()
        categoryName = ""
        pictureStatusOption = PictureStatusProductOption.WITH_AND_WITHOUT
        catalogProductOption = CatalogProductOption.WITH_AND_WITHOUT
        conditionProductOption = ConditionProductOption.ALL_CONDITION
    }}