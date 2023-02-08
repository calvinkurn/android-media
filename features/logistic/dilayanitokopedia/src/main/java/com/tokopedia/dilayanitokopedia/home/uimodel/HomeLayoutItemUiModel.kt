package com.tokopedia.dilayanitokopedia.home.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.dilayanitokopedia.home.constant.HomeLayoutItemState

data class HomeLayoutItemUiModel(
    val layout: Visitable<*>?,
    val state: HomeLayoutItemState,
    val groupId: String?
)
