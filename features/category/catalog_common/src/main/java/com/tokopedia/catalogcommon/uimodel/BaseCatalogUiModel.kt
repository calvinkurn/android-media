package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory
import com.tokopedia.kotlin.model.ImpressHolder

abstract class BaseCatalogUiModel(
    open var idWidget: String = "",
    open var widgetType: String = "",
    open var widgetName: String = "",
    open var widgetBackgroundColor: Int? = null,
    open var widgetTextColor: Int? = null,
    open var darkMode: Boolean = false,
    open var impressHolder: ImpressHolder = ImpressHolder()
) : Visitable<CatalogAdapterFactory>
