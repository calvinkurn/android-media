package com.tokopedia.tokopedianow.common.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.common.adapter.differ.TokoNowCategoryGridDiffer
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryGridAdapterTypeFactory

class TokoNowCategoryGridAdapter(
    typeFactory: TokoNowCategoryGridAdapterTypeFactory,
    differ: TokoNowCategoryGridDiffer
) : BaseTokopediaNowListAdapter<Visitable<*>, TokoNowCategoryGridAdapterTypeFactory>(typeFactory, differ)