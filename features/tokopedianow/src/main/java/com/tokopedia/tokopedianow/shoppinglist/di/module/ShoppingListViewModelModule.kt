package com.tokopedia.tokopedianow.shoppinglist.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.shoppinglist.di.scope.ShoppingListScope
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListAnotherOptionBottomSheetViewModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShoppingListViewModelModule {

    @ShoppingListScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ShoppingListScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoNowShoppingListViewModel::class)
    internal abstract fun shoppingListViewModel(viewModel: TokoNowShoppingListViewModel): ViewModel

    @ShoppingListScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoNowShoppingListAnotherOptionBottomSheetViewModel::class)
    internal abstract fun shoppingListAnotherOptionBottomSheetViewModel(viewModel: TokoNowShoppingListAnotherOptionBottomSheetViewModel): ViewModel
}
