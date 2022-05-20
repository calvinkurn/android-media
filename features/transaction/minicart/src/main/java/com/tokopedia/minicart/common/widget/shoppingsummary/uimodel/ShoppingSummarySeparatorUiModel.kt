package com.tokopedia.minicart.common.widget.shoppingsummary.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.common.widget.shoppingsummary.adapter.ShoppingSummaryAdapterTypeFactory

data class ShoppingSummarySeparatorUiModel(
    var height: Int = DEFAULT_SEPARATOR_HEIGHT
) : Visitable<ShoppingSummaryAdapterTypeFactory> {

    companion object {
        private const val DEFAULT_SEPARATOR_HEIGHT = 1
    }

    override fun type(typeFactory: ShoppingSummaryAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}