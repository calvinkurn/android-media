package com.tokopedia.tokopedianow.datefilter.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.datefilter.presentation.adapter.differ.DateFilterDiffer

class DateFilterAdapter(
    typeFactory: DateFilterAdapterTypeFactory,
    differ: DateFilterDiffer
) : BaseTokopediaNowListAdapter<Visitable<*>, DateFilterAdapterTypeFactory>(typeFactory, differ)