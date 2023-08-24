package com.tokopedia.catalog.ui.model

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class CatalogDetailUiModel(
    val widgets: List<Visitable<*>>
)
