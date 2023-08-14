package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

abstract class BaseCatalogUiModel(
    open val idWidget: String
): Visitable<CatalogAdapterFactory>
