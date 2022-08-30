package com.tokopedia.tokopedianow.recipelist.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.recipelist.presentation.adapter.differ.RecipeFilterDiffer

class RecipeChipFilterAdapter(
    typeFactory: RecipeChipFilterAdapterTypeFactory
) : BaseTokopediaNowListAdapter<Visitable<*>, RecipeChipFilterAdapterTypeFactory>(
    typeFactory,
    RecipeFilterDiffer()
)