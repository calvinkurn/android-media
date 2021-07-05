package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.ProductAdapterFactory

/**
 * Created By @ilhamsuaib on 05/07/21
 */

data class ProductBundleUiModel(
        val bundleName: String,
        val products: List<SomDetailOrder.Data.GetSomDetail.Products>,
        val totalPriceFmt: String
) : BaseProductUiModel {

    override fun type(typeFactory: ProductAdapterFactory): Int {
        return typeFactory.type(this)
    }
}