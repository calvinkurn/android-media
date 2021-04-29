package com.tokopedia.tokomart.home.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.home.base.adapter.BaseHomeListAdapter
import com.tokopedia.tokomart.home.presentation.adapter.differ.TokoMartHomeListDiffer

class TokoMartHomeAdapter(
    typeFactory: TokoMartHomeAdapterTypeFactory,
    differ: TokoMartHomeListDiffer
): BaseHomeListAdapter<Visitable<*>, TokoMartHomeAdapterTypeFactory>(typeFactory, differ)