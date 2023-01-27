package com.tokopedia.shop.home.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class ShopHomeCarouselProductAdapter(
    typeFactory: ShopHomeCarouselProductAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, ShopHomeCarouselProductAdapterTypeFactory>(typeFactory)
