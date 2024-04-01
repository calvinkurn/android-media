package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType

class ShoppingListAdapter(
    typeFactory: ShoppingListAdapterTypeFactory
): BaseTokopediaNowListAdapter<Visitable<*>, ShoppingListAdapterTypeFactory>(typeFactory, ShoppingListDiffer()) {
    fun getHeader(): TokoNowThematicHeaderUiModel? {
        return data.firstOrNull { it is TokoNowThematicHeaderUiModel } as? TokoNowThematicHeaderUiModel
    }

    fun getFirstProductRecommendation(): ShoppingListHorizontalProductCardItemUiModel? {
        return data.firstOrNull { it is ShoppingListHorizontalProductCardItemUiModel && it.productLayoutType == ShoppingListProductLayoutType.PRODUCT_RECOMMENDATION } as? ShoppingListHorizontalProductCardItemUiModel
    }

    fun findPosition(visitable: Visitable<*>): Int {
        return data.indexOf(visitable)
    }
}
