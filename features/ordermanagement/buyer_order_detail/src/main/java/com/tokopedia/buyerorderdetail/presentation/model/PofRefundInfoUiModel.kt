package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager

data class PofRefundInfoUiModel(
    val totalAmountLabel: String,
    val totalAmountValue: String,
    val isRefunded: Boolean,
    val estimateInfoUiModel: EstimateInfoUiModel? = null,
    val pofRefundSummaryUiModel: PofRefundSummaryUiModel? = null
): BaseVisitableUiModel {
    override fun shouldShow(context: Context?): Boolean {
        return totalAmountLabel.isNotBlank() && totalAmountValue.isNotBlank()
    }

    override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
        return null
    }

    override fun type(typeFactory: BuyerOrderDetailTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class EstimateInfoUiModel(
    val title: String,
    val info: String
)
