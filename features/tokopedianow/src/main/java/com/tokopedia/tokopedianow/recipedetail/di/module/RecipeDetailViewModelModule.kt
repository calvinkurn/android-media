package com.tokopedia.tokopedianow.recipedetail.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.recipedetail.di.scope.RecipeDetailScope
import com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel.TokoNowRecipeDetailViewModel
import com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel.TokoNowRecipeSimilarProductViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RecipeDetailViewModelModule {

    @Binds
    @RecipeDetailScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TokoNowRecipeDetailViewModel::class)
    internal abstract fun recipeDetailViewModel(viewModel: TokoNowRecipeDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TokoNowRecipeSimilarProductViewModel::class)
    internal abstract fun recipeSimilarProductViewModel(viewModel: TokoNowRecipeSimilarProductViewModel): ViewModel
}