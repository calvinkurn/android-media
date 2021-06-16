package com.tokopedia.tokomart.categorylist.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.common.base.adapter.BaseTokoMartListAdapter
import com.tokopedia.tokomart.categorylist.presentation.adapter.differ.TokoMartCategoryListDiffer

class TokoMartCategoryListAdapter(
    typeFactory: TokoMartCategoryListAdapterTypeFactory,
    differ: TokoMartCategoryListDiffer
) : BaseTokoMartListAdapter<Visitable<*>, TokoMartCategoryListAdapterTypeFactory>(typeFactory, differ)