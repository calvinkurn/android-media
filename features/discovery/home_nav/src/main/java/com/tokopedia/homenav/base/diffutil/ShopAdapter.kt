package com.tokopedia.homenav.base.diffutil

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.ShopTypeFactory

/**
 * Created by dhaba
 */
class ShopAdapter(
    items: List<Visitable<*>>,
    typeFactory: ShopTypeFactory
) : BaseAdapter<ShopTypeFactory>(typeFactory, items)