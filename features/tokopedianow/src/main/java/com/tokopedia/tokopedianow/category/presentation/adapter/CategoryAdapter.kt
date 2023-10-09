package com.tokopedia.tokopedianow.category.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowDiffer
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter

class CategoryAdapter(
    typeFactory: BaseAdapterTypeFactory,
    differ: BaseTokopediaNowDiffer
): BaseTokopediaNowListAdapter<Visitable<*>, BaseAdapterTypeFactory>(typeFactory, differ)
