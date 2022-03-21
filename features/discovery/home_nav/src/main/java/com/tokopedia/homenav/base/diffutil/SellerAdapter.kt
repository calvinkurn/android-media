package com.tokopedia.homenav.base.diffutil

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.SellerTypeFactory

/**
 * Created by dhaba
 */
class SellerAdapter(
    items: List<Visitable<*>>,
    typeFactory: SellerTypeFactory
) : BaseAdapter<SellerTypeFactory>(typeFactory, items)