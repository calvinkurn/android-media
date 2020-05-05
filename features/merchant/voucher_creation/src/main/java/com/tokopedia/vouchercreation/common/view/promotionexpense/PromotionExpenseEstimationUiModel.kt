package com.tokopedia.vouchercreation.common.view.promotionexpense

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.common.view.VoucherCommonTypeFactory

class PromotionExpenseEstimationUiModel(
        val isHaveMargin: Boolean = true,
        val isHaveToolTip: Boolean = true,
        val onTooltipClicked: () -> Unit = {},
        var estimationValue: Int = 0
) : Visitable<VoucherCommonTypeFactory> {

    override fun type(typeFactory: VoucherCommonTypeFactory): Int =
            typeFactory.type(this)
}