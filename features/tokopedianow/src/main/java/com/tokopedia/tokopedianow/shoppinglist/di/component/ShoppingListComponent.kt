package com.tokopedia.tokopedianow.shoppinglist.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.shoppinglist.di.module.ShoppingListContextModule
import com.tokopedia.tokopedianow.shoppinglist.di.module.ShoppingListViewModelModule
import com.tokopedia.tokopedianow.shoppinglist.di.scope.ShoppingListScope
import com.tokopedia.tokopedianow.searchcategory.di.UserSessionModule
import com.tokopedia.tokopedianow.shoppinglist.presentation.fragment.TokoNowShoppingListFragment
import dagger.Component

@ShoppingListScope
@Component(
        modules = [
            ShoppingListViewModelModule::class,
            ShoppingListContextModule::class,
            UserSessionModule::class
        ],
        dependencies = [BaseAppComponent::class])
interface ShoppingListComponent {
    fun inject(fragment: TokoNowShoppingListFragment)
}
