package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactory

data class SomBmgmUiModel(
    override val bmgmId: String,
    override val bmgmName: String,
    override val bmgmIconUrl: String,
    override val totalPrice: Double,
    override val totalPriceText: String,
    override val totalPriceReductionInfoText: String,
    override val bmgmItemList: List<ProductUiModel>
) : ProductBmgmSectionUiModel(
    bmgmId,
    bmgmName,
    bmgmIconUrl,
    totalPrice,
    totalPriceText,
    totalPriceReductionInfoText,
    bmgmItemList
), BaseProductUiModel {
    override fun type(typeFactory: SomDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
