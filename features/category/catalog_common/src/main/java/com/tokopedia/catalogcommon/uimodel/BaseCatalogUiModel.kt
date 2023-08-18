package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

abstract class BaseCatalogUiModel(
    open val idWidget: String,
    open val widgetType: String,
    open val widgetName: String,
    open val widgetBackgroundColor: Int? = null,
    open val widgetTextColor: Int? = null
) : Visitable<CatalogAdapterFactory>
