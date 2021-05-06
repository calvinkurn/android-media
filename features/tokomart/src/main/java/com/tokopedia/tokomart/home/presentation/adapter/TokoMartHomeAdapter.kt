package com.tokopedia.tokomart.home.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.base.adapter.BaseTokoMartListAdapter
import com.tokopedia.tokomart.home.presentation.adapter.differ.TokoMartHomeListDiffer

class TokoMartHomeAdapter(
    typeFactory: TokoMartHomeAdapterTypeFactory,
    differ: TokoMartHomeListDiffer
): BaseTokoMartListAdapter<Visitable<*>, TokoMartHomeAdapterTypeFactory>(typeFactory, differ)