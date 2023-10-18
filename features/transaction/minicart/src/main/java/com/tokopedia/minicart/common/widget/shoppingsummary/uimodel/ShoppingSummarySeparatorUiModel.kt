package com.tokopedia.minicart.common.widget.shoppingsummary.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.common.widget.shoppingsummary.adapter.ShoppingSummaryAdapterTypeFactory

class ShoppingSummarySeparatorUiModel : Visitable<ShoppingSummaryAdapterTypeFactory> {

    override fun type(typeFactory: ShoppingSummaryAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
