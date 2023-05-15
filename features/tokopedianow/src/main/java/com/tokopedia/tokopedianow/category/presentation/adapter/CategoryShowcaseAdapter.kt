package com.tokopedia.tokopedianow.category.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryShowcaseAdapterTypeFactory
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter

class CategoryShowcaseAdapter(
    typeFactory: CategoryShowcaseAdapterTypeFactory,
    differ: CategoryDiffer
): BaseTokopediaNowListAdapter<Visitable<*>, CategoryShowcaseAdapterTypeFactory>(typeFactory, differ)
