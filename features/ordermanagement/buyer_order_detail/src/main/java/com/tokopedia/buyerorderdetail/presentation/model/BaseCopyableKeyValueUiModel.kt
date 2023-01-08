package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager

abstract class BaseCopyableKeyValueUiModel : BaseVisitableUiModel {

    abstract val copyableText: String
    abstract val copyLabel: StringRes
    abstract val copyMessage: StringRes
    abstract val label: StringRes

    override fun shouldShow(context: Context?): Boolean {
        return copyableText.isNotBlank()
    }

    override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
        return null
    }
}
