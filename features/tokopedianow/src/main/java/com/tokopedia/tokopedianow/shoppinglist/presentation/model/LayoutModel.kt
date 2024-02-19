package com.tokopedia.tokopedianow.shoppinglist.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class LayoutModel(
    val layout: List<Visitable<*>> = emptyList(),
    val isRequiredToScrollUp: Boolean = false
)
