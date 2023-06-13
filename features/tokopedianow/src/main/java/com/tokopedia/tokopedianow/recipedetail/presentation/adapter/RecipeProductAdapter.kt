package com.tokopedia.tokopedianow.recipedetail.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.differ.RecipeProductDiffer

class RecipeProductAdapter(
    typeFactory: RecipeProductAdapterTypeFactory
) : BaseTokopediaNowListAdapter<Visitable<*>, RecipeProductAdapterTypeFactory>(
    typeFactory,
    RecipeProductDiffer()
)