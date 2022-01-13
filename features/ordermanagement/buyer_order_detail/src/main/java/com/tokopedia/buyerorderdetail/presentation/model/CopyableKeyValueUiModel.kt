package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero

open class CopyableKeyValueUiModel(
    open val copyableText: String = "",
    open val copyLabel: StringRes = StringRes(Int.ZERO),
    open val copyMessage: StringRes = StringRes(Int.ZERO),
    open val label: StringRes = StringRes(Int.ZERO)
) : BaseVisitableUiModel {
    override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    override fun shouldShow(context: Context?): Boolean {
        return copyableText.isNotBlank()
    }

    override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
        return null
    }
}