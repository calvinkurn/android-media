package com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.tkpd.flashsale.domain.entity.SubmittedProduct
import com.tokopedia.tkpd.flashsale.domain.entity.enums.ProductStockStatus

data class OnSelectionProcessItem(
    val campaignStock: Int,
    val isMultiwarehouse: Boolean,
    val isParentProduct: Boolean,
    val totalChild: Int,
    val mainStock: Int,
    val name: String,
    val picture: String,
    val productCriteria: SubmittedProduct.ProductCriteria,
    val productId: Long,
    val url: String,
    val price: SubmittedProduct.Price,
    val discount: SubmittedProduct.Discount,
    val discountedPrice: SubmittedProduct.DiscountedPrice,
    val submittedProductStockStatus: ProductStockStatus,
    val warehouses: List<SubmittedProduct.Warehouse>,
    val countLocation: Int
) : DelegateAdapterItem {
    override fun id() = productId
}
