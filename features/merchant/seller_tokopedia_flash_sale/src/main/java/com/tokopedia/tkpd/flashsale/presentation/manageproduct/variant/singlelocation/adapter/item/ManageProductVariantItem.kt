package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.adapter.item

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct

data class ManageProductVariantItem(
    val disabledReason: String,
    val isDisabled: Boolean,
    val isMultiwarehouse: Boolean,
    var isToggleOn: Boolean,
    val name: String,
    val picture: String,
    val price: ReservedProduct.Product.Price,
    val productId: Long,
    val sku: String,
    val stock: Int,
    val url: String,
    val warehouses: List<ReservedProduct.Product.Warehouse>
): DelegateAdapterItem {
    override fun id() = productId
}