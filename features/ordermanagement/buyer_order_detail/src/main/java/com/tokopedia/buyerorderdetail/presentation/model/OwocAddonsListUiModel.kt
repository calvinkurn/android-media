package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocProductListTypeFactoryImpl
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel

data class OwocAddonsListUiModel(
    val addOnSummaryUiModel: AddOnSummaryUiModel?
) : BaseOwocVisitableUiModel {

    override fun type(typeFactory: OwocProductListTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    override fun shouldShow(context: Context?): Boolean {
        return addOnSummaryUiModel != null && addOnSummaryUiModel.addonsLogoUrl.isNotBlank()
                && addOnSummaryUiModel.addonsTitle.isNotBlank()
    }
}
