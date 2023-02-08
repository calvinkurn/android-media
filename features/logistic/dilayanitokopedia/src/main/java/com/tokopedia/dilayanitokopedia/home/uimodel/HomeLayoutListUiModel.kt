package com.tokopedia.dilayanitokopedia.home.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutState

data class HomeLayoutListUiModel(
    val items: List<Visitable<*>>,
    @DtLayoutState val state: Int = 0
)
