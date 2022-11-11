package com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.item

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct

data class FlashSaleManageProductListItem(
    val product: ReservedProduct.Product? = null
) : DelegateAdapterItem {
    override fun id() = product?.productId.orZero()
}