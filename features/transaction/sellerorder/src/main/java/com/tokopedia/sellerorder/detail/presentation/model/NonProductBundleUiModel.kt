package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.ProductAdapterFactory

/**
 * Created By @ilhamsuaib on 05/07/21
 */

data class NonProductBundleUiModel(
        val product: SomDetailOrder.Data.GetSomDetail.Products
) : BaseProductUiModel {

    override fun type(typeFactory: ProductAdapterFactory): Int {
        return typeFactory.type(this)
    }
}