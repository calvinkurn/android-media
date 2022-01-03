package com.tokopedia.tokopedianow.common.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.common.adapter.differ.TokoNowCategoryGridDiffer
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryChipsAdapterTypeFactory

class TokoNowCategoryChipsAdapter(
    typeFactory: TokoNowCategoryChipsAdapterTypeFactory,
    differ: TokoNowCategoryGridDiffer
) : BaseTokopediaNowListAdapter<Visitable<*>, TokoNowCategoryChipsAdapterTypeFactory>(typeFactory, differ)