package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactory

/**
 * Created By @ilhamsuaib on 05/07/21
 */

data class ProductBundleUiModel(
    val bundleId: String = "",
    val bundleIcon: String = "",
    val bundleName: String = "",
    val bundlePrice: String = "",
    val bundleSubTotal: String = "",
    val products: List<ProductUiModel>
) : BaseProductUiModel {

    override fun type(typeFactory: SomDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    data class ProductUiModel(
        val detail: SomDetailOrder.GetSomDetail.Details.Product,
        val addOnSummaryUiModel: AddOnSummaryUiModel? = null,
    )
}
