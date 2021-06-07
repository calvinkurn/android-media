package com.tokopedia.minicart.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.cartlist.MiniCartSummaryTransactionUiModel
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData

data class MiniCartListUiModel(
        var title: String = "",
        var maximumShippingWeight: Int = 0,
        var maximumShippingWeightErrorMessage: String = "",
        var miniCartWidgetUiModel: MiniCartWidgetData = MiniCartWidgetData(),
        var miniCartSummaryTransactionUiModel: MiniCartSummaryTransactionUiModel = MiniCartSummaryTransactionUiModel(),
        var visitables: MutableList<Visitable<*>> = mutableListOf()
)