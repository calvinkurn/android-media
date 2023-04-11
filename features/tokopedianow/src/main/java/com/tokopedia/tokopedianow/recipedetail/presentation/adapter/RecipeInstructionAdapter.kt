package com.tokopedia.tokopedianow.recipedetail.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.differ.RecipeInstructionDiffer

class RecipeInstructionAdapter(
    typeFactory: RecipeInstructionAdapterTypeFactory
) : BaseTokopediaNowListAdapter<Visitable<*>, RecipeInstructionAdapterTypeFactory>(
    typeFactory,
    RecipeInstructionDiffer()
)