package com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.widget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeBudgetTypeFactory

class PromotionTypeInputUiModel : Visitable<PromotionTypeBudgetTypeFactory> {

    override fun type(typeFactory: PromotionTypeBudgetTypeFactory): Int =
            typeFactory.type(this)
}