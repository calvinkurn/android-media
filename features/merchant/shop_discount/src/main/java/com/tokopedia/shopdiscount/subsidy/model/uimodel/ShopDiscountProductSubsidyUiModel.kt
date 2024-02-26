package com.tokopedia.shopdiscount.subsidy.model.uimodel

import android.os.Parcelable
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopDiscountProductSubsidyUiModel(
    val productDetailData: ShopDiscountProductDetailUiModel.ProductDetailData = ShopDiscountProductDetailUiModel.ProductDetailData(),
    var isSelected: Boolean = false
) : Parcelable {
}
