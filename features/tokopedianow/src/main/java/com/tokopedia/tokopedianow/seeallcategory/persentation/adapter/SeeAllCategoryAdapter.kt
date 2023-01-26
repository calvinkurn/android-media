package com.tokopedia.tokopedianow.seeallcategory.persentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.seeallcategory.persentation.adapter.differ.SeeAllCategoryDiffer

class SeeAllCategoryAdapter(
    typeFactory: SeeAllCategoryAdapterTypeFactory
): BaseTokopediaNowListAdapter<Visitable<*>, SeeAllCategoryAdapterTypeFactory>(typeFactory, SeeAllCategoryDiffer())
