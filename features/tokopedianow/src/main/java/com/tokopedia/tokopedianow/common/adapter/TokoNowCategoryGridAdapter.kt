package com.tokopedia.tokopedianow.common.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.common.adapter.differ.TokoNowCategoryGridDiffer
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryMenuAdapterTypeFactory

class TokoNowCategoryGridAdapter(
    typeFactory: TokoNowCategoryMenuAdapterTypeFactory,
    differ: TokoNowCategoryGridDiffer
) : BaseTokopediaNowListAdapter<Visitable<*>, TokoNowCategoryMenuAdapterTypeFactory>(typeFactory, differ)
