package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.anotheroptionbottomsheet

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter

class ShoppingListAnotherOptionBottomSheetAdapter(
    typeFactory: ShoppingListAnotherOptionBottomSheetAdapterTypeFactory
) : BaseTokopediaNowListAdapter<Visitable<*>, ShoppingListAnotherOptionBottomSheetAdapterTypeFactory>(
    typeFactory,
    ShoppingListAnotherOptionBottomSheetDiffer()
)
