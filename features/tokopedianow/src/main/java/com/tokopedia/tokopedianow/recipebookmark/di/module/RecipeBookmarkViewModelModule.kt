package com.tokopedia.tokopedianow.recipebookmark.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.recipebookmark.di.scope.RecipeBookmarkScope
import com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel.TokoNowRecipeDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RecipeBookmarkViewModelModule {

    @Binds
    @RecipeBookmarkScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TokoNowRecipeDetailViewModel::class)
    internal abstract fun recipeDetailViewModel(recipeDetailViewModel: TokoNowRecipeDetailViewModel): ViewModel
}