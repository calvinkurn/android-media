package com.tokopedia.tokopedianow.recipesearch.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.recipesearch.presentation.viewmodel.TokoNowRecipeSearchViewModel
import com.tokopedia.tokopedianow.recipesearch.di.scope.RecipeSearchScope
import com.tokopedia.tokopedianow.recipesearch.presentation.viewmodel.TokoNowRecipeSearchIngredientViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RecipeSearchViewModelModule {

    @Binds
    @RecipeSearchScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @RecipeSearchScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoNowRecipeSearchViewModel::class)
    internal abstract fun recipeSearchViewModel(viewModel: TokoNowRecipeSearchViewModel): ViewModel

    @RecipeSearchScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoNowRecipeSearchIngredientViewModel::class)
    internal abstract fun recipeSearchIngredientViewModel(viewModel: TokoNowRecipeSearchIngredientViewModel): ViewModel
}