package com.tokopedia.tokopedianow.category.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryShowcaseDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryShowcaseAdapterTypeFactory
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter

class CategoryShowcaseAdapter(
    typeFactory: CategoryShowcaseAdapterTypeFactory,
    differ: CategoryShowcaseDiffer
): BaseTokopediaNowListAdapter<Visitable<*>, CategoryShowcaseAdapterTypeFactory>(typeFactory, differ)
