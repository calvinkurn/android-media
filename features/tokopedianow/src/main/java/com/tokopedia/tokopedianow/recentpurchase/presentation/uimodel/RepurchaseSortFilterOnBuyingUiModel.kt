package com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.RepurchaseSortFilterOnBuyingTypeFactory

data class RepurchaseSortFilterOnBuyingUiModel(
    var titleRes: Int,
    var isChecked: Boolean,
    var isLastItem: Boolean,
    var value: Int
) : Visitable<RepurchaseSortFilterOnBuyingTypeFactory>, ImpressHolder() {

    override fun type(typeFactory: RepurchaseSortFilterOnBuyingTypeFactory): Int {
        return typeFactory.type(this)
    }
}

