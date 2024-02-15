package com.tokopedia.tokopedianow.shoppinglist.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.shoppinglist.di.module.ShoppingListViewModelModule
import com.tokopedia.tokopedianow.shoppinglist.di.scope.ShoppingListScope
import com.tokopedia.tokopedianow.shoppinglist.di.module.ShoppingListModule
import com.tokopedia.tokopedianow.shoppinglist.presentation.bottomsheet.TokoNowShoppingListAnotherOptionBottomSheet
import com.tokopedia.tokopedianow.shoppinglist.presentation.fragment.TokoNowShoppingListFragment
import dagger.Component

@ShoppingListScope
@Component(
        modules = [
            ShoppingListModule::class,
            ShoppingListViewModelModule::class,
        ],
        dependencies = [BaseAppComponent::class])
interface ShoppingListComponent {
    fun inject(fragment: TokoNowShoppingListFragment)
    fun inject(bottomSheet: TokoNowShoppingListAnotherOptionBottomSheet)
}
