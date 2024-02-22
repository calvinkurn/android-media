package com.tokopedia.shopdiscount.subsidy.model.mapper

import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageEntrySource
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailUiModel
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountManageProductSubsidyUiModel

object ShopDiscountManageProductSubsidyUiModelMapper {
    fun map(
        listProductDetailData: List<ShopDiscountProductDetailUiModel.ProductDetailData>,
        mode: String,
        entrySource: ShopDiscountManageEntrySource
    ): ShopDiscountManageProductSubsidyUiModel {
        return ShopDiscountManageProductSubsidyUiModel(
            listProductDetailData = listProductDetailData,
            mode = mode,
            entrySource = entrySource,
            hasNonSubsidyProduct = listProductDetailData.any { !it.isSubsidy }
        )
    }
}
