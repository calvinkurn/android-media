package com.tokopedia.tokopedianow.category.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryAdapterTypeFactory
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter

class CategoryAdapter(
    typeFactory: CategoryAdapterTypeFactory,
    differ: CategoryDiffer
): BaseTokopediaNowListAdapter<Visitable<*>, CategoryAdapterTypeFactory>(typeFactory, differ)
