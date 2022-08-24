package com.tokopedia.tokopedianow.recipelist.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.recipelist.presentation.adapter.differ.RecipeFilterDiffer

class RecipeChipAdapter(
    typeFactory: RecipeChipAdapterTypeFactory
) : BaseTokopediaNowListAdapter<Visitable<*>, RecipeChipAdapterTypeFactory>(
    typeFactory,
    RecipeFilterDiffer()
)