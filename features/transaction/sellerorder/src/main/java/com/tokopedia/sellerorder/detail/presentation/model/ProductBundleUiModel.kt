package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.ProductAdapterFactory

/**
 * Created By @ilhamsuaib on 05/07/21
 */

data class ProductBundleUiModel(
        val bundleId: String = "",
        val bundleName: String = "",
        val bundlePrice: String = "",
        val quantity: Int = 0,
        val bundleSubTotal: String = "",
        val orderDetail: List<SomDetailOrder.Data.GetSomDetail.Products>,
) : BaseProductUiModel {

    override fun type(typeFactory: ProductAdapterFactory): Int {
        return typeFactory.type(this)
    }
}