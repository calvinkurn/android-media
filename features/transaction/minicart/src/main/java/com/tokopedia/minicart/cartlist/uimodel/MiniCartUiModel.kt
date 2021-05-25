package com.tokopedia.minicart.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData

data class MiniCartUiModel(
        var title: String = "",
        var miniCartWidgetData: MiniCartWidgetData = MiniCartWidgetData(),
        var visitables: MutableList<Visitable<*>> = mutableListOf()
)