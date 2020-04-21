package com.tokopedia.product.manage.oldlist.data.model

import android.os.Parcelable
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant
import com.tokopedia.product.manage.oldlist.constant.option.CatalogProductOption
import com.tokopedia.product.manage.oldlist.constant.option.ConditionProductOption
import com.tokopedia.product.manage.oldlist.constant.option.PictureStatusProductOption
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductManageFilterModel(
        var etalaseProductOption: Int = ProductManageListConstant.FILTER_ALL_PRODUK,
        var etalaseProductOptionName: String = "",
        var pictureStatusOption: String = PictureStatusProductOption.WITH_AND_WITHOUT,
        var conditionProductOption: String = ConditionProductOption.ALL_CONDITION,
        var catalogProductOption: String = CatalogProductOption.WITH_AND_WITHOUT,
        var categoryId: String = ProductManageListConstant.FILTER_ALL_CATEGORY.toString(),
        var categoryName: String = ""
) : Parcelable {

    fun reset() {
        etalaseProductOption = ProductManageListConstant.FILTER_ALL_PRODUK
        etalaseProductOptionName = ""
        categoryId = ProductManageListConstant.FILTER_ALL_CATEGORY.toString()
        categoryName = ""
        pictureStatusOption = PictureStatusProductOption.WITH_AND_WITHOUT
        catalogProductOption = CatalogProductOption.WITH_AND_WITHOUT
        conditionProductOption = ConditionProductOption.ALL_CONDITION
    }}