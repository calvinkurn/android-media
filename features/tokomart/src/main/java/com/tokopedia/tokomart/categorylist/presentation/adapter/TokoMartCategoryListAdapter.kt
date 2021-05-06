package com.tokopedia.tokomart.categorylist.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.home.base.adapter.BaseHomeListAdapter
import com.tokopedia.tokomart.home.presentation.adapter.differ.TokoMartHomeListDiffer

class TokoMartCategoryListAdapter(
    typeFactory: TokoMartCategoryListAdapterTypeFactory,
    differ: TokoMartHomeListDiffer
): BaseHomeListAdapter<Visitable<*>, TokoMartCategoryListAdapterTypeFactory>(typeFactory, differ)