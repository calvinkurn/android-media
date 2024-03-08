package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactory

data class SomDetailAddOnOrderLevelUiModel(
    val addOnSummaryUiModel: AddOnSummaryUiModel? = null,
) : BaseProductUiModel {

    override fun type(typeFactory: SomDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
