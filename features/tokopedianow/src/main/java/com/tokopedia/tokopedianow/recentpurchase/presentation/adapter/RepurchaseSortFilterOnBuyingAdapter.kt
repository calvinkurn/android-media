package com.tokopedia.tokopedianow.recentpurchase.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.differ.RepurchaseSortFilterOnBuyingDiffer

class RepurchaseSortFilterOnBuyingAdapter(
    typeFactory: RepurchaseSortFilterOnBuyingAdapterTypeFactory,
    differ: RepurchaseSortFilterOnBuyingDiffer
) : BaseTokopediaNowListAdapter<Visitable<*>, RepurchaseSortFilterOnBuyingAdapterTypeFactory>(typeFactory, differ)