package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager
import java.io.Serializable

interface BaseVisitableUiModel : Visitable<BuyerOrderDetailTypeFactory>, Serializable {
    fun shouldShow(context: Context?): Boolean
    fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager?
}
