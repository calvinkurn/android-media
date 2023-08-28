package com.tokopedia.catalog.ui.model

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class CatalogDetailUiModel(
    val widgets: List<Visitable<*>> = emptyList(),
    val navigationProperties: NavigationProperties
)

data class NavigationProperties (
    val isDarkMode: Boolean = false,
    val isPremium: Boolean = false,
    val bgColor: Int = 0
)
