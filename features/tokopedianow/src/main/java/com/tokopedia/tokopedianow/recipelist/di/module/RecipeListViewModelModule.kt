package com.tokopedia.tokopedianow.recipelist.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.recipelist.di.scope.RecipeListScope
import com.tokopedia.tokopedianow.recipelist.presentation.viewmodel.TokoNowRecipeFilterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RecipeListViewModelModule {

    @Binds
    @RecipeListScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @RecipeListScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoNowRecipeFilterViewModel::class)
    internal abstract fun recipeFilterViewModel(viewModel: TokoNowRecipeFilterViewModel): ViewModel
}