package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter

class ShoppingListAdapter(
    typeFactory: ShoppingListAdapterTypeFactory
): BaseTokopediaNowListAdapter<Visitable<*>, ShoppingListAdapterTypeFactory>(typeFactory, ShoppingListDiffer())
