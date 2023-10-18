package com.tokopedia.minicart.common.widget.shoppingsummary.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.common.widget.shoppingsummary.adapter.ShoppingSummaryTypeFactory

data class ShoppingSummaryHeaderUiModel(
    val iconUrl: String,
    val title: String,
    val description: String
) : Visitable<ShoppingSummaryTypeFactory> {

    override fun type(typeFactory: ShoppingSummaryTypeFactory): Int {
        return typeFactory.type(this)
    }
}
