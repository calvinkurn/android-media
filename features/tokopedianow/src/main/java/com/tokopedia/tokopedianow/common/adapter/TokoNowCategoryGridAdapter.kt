package com.tokopedia.tokopedianow.common.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.common.adapter.differ.TokoNowCategoryMenuDiffer
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryMenuAdapterTypeFactory

class TokoNowCategoryMenuAdapter(
    typeFactory: TokoNowCategoryMenuAdapterTypeFactory,
    differ: TokoNowCategoryMenuDiffer
) : BaseTokopediaNowListAdapter<Visitable<*>, TokoNowCategoryMenuAdapterTypeFactory>(typeFactory, differ)
