package com.tokopedia.tokopedianow.recipeautocomplete.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.recipeautocomplete.di.scope.RecipeAutoCompleteScope
import com.tokopedia.tokopedianow.recipeautocomplete.presentation.viewmodel.TokoNowRecipeAutoCompleteViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RecipeAutoCompleteViewModelModule {

    @Binds
    @RecipeAutoCompleteScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @RecipeAutoCompleteScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoNowRecipeAutoCompleteViewModel::class)
    internal abstract fun recipeSearchViewModel(viewModel: TokoNowRecipeAutoCompleteViewModel): ViewModel
}