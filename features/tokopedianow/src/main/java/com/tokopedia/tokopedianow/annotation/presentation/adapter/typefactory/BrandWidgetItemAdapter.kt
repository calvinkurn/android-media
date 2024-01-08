package com.tokopedia.tokopedianow.annotation.presentation.adapter.typefactory

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.tokopedianow.annotation.analytic.AnnotationWidgetAnalytic

class BrandWidgetItemAdapter(
    analytic: AnnotationWidgetAnalytic
) : BaseListAdapter<Visitable<*>, BrandWidgetItemAdapterTypeFactory>(
    BrandWidgetItemAdapterTypeFactory(analytic)
)
