package com.tokopedia.dilayanitokopedia.ui.home.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.dilayanitokopedia.ui.home.constant.HomeLayoutItemState

data class HomeLayoutItemUiModel(
    val layout: Visitable<*>?,
    val state: HomeLayoutItemState,
    val groupId: String?
)
