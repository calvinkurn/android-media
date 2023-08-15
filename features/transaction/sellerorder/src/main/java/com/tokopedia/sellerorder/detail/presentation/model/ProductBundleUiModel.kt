package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactoryImpl

/**
 * Created By @ilhamsuaib on 05/07/21
 */

data class ProductBundleUiModel(
    val bundleId: String = "",
    val bundleIcon: String = "",
    val bundleName: String = "",
    val bundlePrice: String = "",
    val bundleSubTotal: String = "",
    val orderDetail: List<SomDetailOrder.Data.GetSomDetail.Details.Product>,
) : BaseProductUiModel {

    override fun type(typeFactory: SomDetailAdapterFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
