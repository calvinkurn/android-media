package com.tokopedia.shopdiscount.subsidy.model.mapper

import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailUiModel
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountProductSubsidyUiModel

object ShopDiscountProductSubsidyMapper {


    fun map(
        listProductDetailSubsidyData: List<ShopDiscountProductDetailUiModel.ProductDetailData>,
    ): List<ShopDiscountProductSubsidyUiModel> {
        return listProductDetailSubsidyData.map {
            ShopDiscountProductSubsidyUiModel(
                productDetailData = it
            )
        }
    }
}
