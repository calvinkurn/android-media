package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.differ.ShoppingListDiffer

class ShoppingListAdapter(
    typeFactory: ShoppingListAdapterTypeFactory
): BaseTokopediaNowListAdapter<Visitable<*>, ShoppingListAdapterTypeFactory>(typeFactory, ShoppingListDiffer())
