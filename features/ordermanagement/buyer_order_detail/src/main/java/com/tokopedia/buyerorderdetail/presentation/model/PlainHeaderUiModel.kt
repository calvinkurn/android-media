package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager
import com.tokopedia.kotlin.extensions.view.orZero

data class PlainHeaderUiModel(
    val header: StringRes
) : BaseVisitableUiModel {
    override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    override fun shouldShow(context: Context?): Boolean {
        return header.getStringValue(context).isNotBlank()
    }

    override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
        return null
    }
}
