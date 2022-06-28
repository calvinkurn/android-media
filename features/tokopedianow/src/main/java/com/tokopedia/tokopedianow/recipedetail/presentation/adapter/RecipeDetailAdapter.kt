package com.tokopedia.tokopedianow.recipedetail.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.differ.RecipeDetailDiffer

class RecipeDetailAdapter(
    typeFactory: RecipeDetailAdapterTypeFactory
): BaseTokopediaNowListAdapter<Visitable<*>, RecipeDetailAdapterTypeFactory>(typeFactory, RecipeDetailDiffer())