package com.tokopedia.tokopedianow.recipebookmark.persentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.recipebookmark.persentation.adapter.differ.RecipeBookmarkDiffer

class RecipeBookmarkAdapter(
    typeFactory: RecipeBookmarkAdapterTypeFactory
): BaseTokopediaNowListAdapter<Visitable<*>, RecipeBookmarkAdapterTypeFactory>(typeFactory, RecipeBookmarkDiffer())