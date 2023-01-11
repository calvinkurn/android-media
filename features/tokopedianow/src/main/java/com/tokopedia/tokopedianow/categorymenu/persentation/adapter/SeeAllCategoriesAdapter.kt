package com.tokopedia.tokopedianow.categorymenu.persentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.categorymenu.persentation.adapter.differ.SeeAllCategoriesDiffer

class SeeAllCategoriesAdapter(
    typeFactory: SeeAllCategoriesAdapterTypeFactory
): BaseTokopediaNowListAdapter<Visitable<*>, SeeAllCategoriesAdapterTypeFactory>(typeFactory, SeeAllCategoriesDiffer())
