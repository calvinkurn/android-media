package com.tokopedia.tokopedianow.recipehome.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.recipehome.di.scope.RecipeHomeScope
import com.tokopedia.tokopedianow.recipehome.presentation.viewmodel.TokoNowRecipeHomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RecipeHomeViewModelModule {

    @Binds
    @RecipeHomeScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @RecipeHomeScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoNowRecipeHomeViewModel::class)
    internal abstract fun categoryViewModel(viewModel: TokoNowRecipeHomeViewModel): ViewModel
}