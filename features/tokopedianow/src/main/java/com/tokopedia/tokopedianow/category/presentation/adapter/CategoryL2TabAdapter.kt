package com.tokopedia.tokopedianow.category.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryL2TabDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryL2TabAdapterTypeFactory
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter

class CategoryL2TabAdapter(
    typeFactory: CategoryL2TabAdapterTypeFactory,
    differ: CategoryL2TabDiffer
) : BaseTokopediaNowListAdapter<Visitable<*>, BaseAdapterTypeFactory>(typeFactory, differ)
