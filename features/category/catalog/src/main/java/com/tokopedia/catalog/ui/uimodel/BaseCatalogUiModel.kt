package com.tokopedia.catalog.ui.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.catalog.ui.adapter.CatalogAdapterFactory

abstract class BaseCatalogUiModel(
    open val idWidget: String
): Visitable<CatalogAdapterFactory>
