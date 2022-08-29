package com.tokopedia.addongifting.addonbottomsheet.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class FragmentUiModel(
        var hasLoadedData: Boolean = false,
        var recyclerViewItems: List<Visitable<*>> = emptyList(),
        var totalAmount: TotalAmountUiModel = TotalAmountUiModel()
)