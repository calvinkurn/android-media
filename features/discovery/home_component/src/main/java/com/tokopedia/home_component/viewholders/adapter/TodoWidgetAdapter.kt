package com.tokopedia.home_component.viewholders.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory

/**
 * Created by frenzel
 */
class TodoWidgetAdapter(
    items: List<Visitable<*>>,
    typeFactory: CommonCarouselProductCardTypeFactory
) : BaseAdapter<CommonCarouselProductCardTypeFactory>(typeFactory, items)
