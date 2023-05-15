package com.tokopedia.tokopedianow.category.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryNavigationAdapterTypeFactory
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter

class CategoryNavigationAdapter(
    typeFactory: CategoryNavigationAdapterTypeFactory,
    differ: CategoryDiffer
): BaseTokopediaNowListAdapter<Visitable<*>, CategoryNavigationAdapterTypeFactory>(typeFactory, differ)
