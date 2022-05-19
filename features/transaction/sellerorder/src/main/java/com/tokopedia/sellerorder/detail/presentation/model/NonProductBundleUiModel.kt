package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactory

/**
 * Created By @ilhamsuaib on 05/07/21
 */

data class NonProductBundleUiModel(
    val product: SomDetailOrder.Data.GetSomDetail.Details.Product? = null,
    val addOnSummary: AddOnSummaryUiModel? = null
) : BaseProductUiModel {

    override fun type(typeFactory: SomDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}