package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager

interface BaseVisitableUiModel: Visitable<BuyerOrderDetailTypeFactory> {
    fun shouldShow(): Boolean
    fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager?
}