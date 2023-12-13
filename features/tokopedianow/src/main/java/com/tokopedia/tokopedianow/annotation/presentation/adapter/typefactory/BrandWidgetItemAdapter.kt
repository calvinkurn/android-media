package com.tokopedia.tokopedianow.annotation.presentation.adapter.typefactory

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class BrandWidgetItemAdapter: BaseListAdapter<Visitable<*>, BrandWidgetItemAdapterTypeFactory>(
    BrandWidgetItemAdapterTypeFactory()
)
