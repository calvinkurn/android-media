package com.tokopedia.tokopedianow.sortfilter.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.sortfilter.presentation.differ.SortFilterDiffer
import com.tokopedia.tokopedianow.sortfilter.presentation.typefactory.SortFilterAdapterTypeFactory

class SortFilterAdapter(
    typeFactory: SortFilterAdapterTypeFactory,
    differ: SortFilterDiffer
) : BaseTokopediaNowListAdapter<Visitable<*>, SortFilterAdapterTypeFactory>(typeFactory, differ)