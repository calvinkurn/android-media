package com.tokopedia.tokopedianow.repurchase.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.repurchase.presentation.adapter.differ.RepurchaseListDiffer

class RepurchaseAdapter(
    typeFactory: RepurchaseAdapterTypeFactory,
    differ: RepurchaseListDiffer
) : BaseTokopediaNowListAdapter<Visitable<*>, RepurchaseAdapterTypeFactory>(typeFactory, differ)
