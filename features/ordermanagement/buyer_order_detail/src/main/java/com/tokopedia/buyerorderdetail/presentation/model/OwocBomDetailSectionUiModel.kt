package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager

data class OwocBomDetailSectionUiModel(
    val txId: String,
    val groupType: String,
    val sectionTitle: String,
    val sectionDesc: String,
    val imageUrl: String
) : BaseVisitableUiModel {
    override fun shouldShow(context: Context?): Boolean {
        return sectionTitle.isNotBlank() && sectionDesc.isNotBlank()
    }

    override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
        return null
    }

    override fun type(typeFactory: BuyerOrderDetailTypeFactory): Int {
        return typeFactory.type(this)
    }
}
