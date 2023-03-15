package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager
import com.tokopedia.kotlin.extensions.view.orZero

data class TickerUiModel(
    val actionKey: String,
    val actionText: String,
    val actionUrl: String,
    val description: String,
    val type: String
) : BaseVisitableUiModel {

    var marginBottom: Int? = null

    var marginTop: Int? = null

    override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    override fun shouldShow(context: Context?): Boolean {
        return description.isNotBlank() || actionText.isNotBlank()
    }

    override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
        return null
    }
}
