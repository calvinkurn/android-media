package com.tokopedia.tokopedianow.recipesearch.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.recipesearch.di.module.RecipeSearchModule
import com.tokopedia.tokopedianow.recipesearch.di.module.RecipeSearchViewModelModule
import com.tokopedia.tokopedianow.recipesearch.presentation.fragment.TokoNowRecipeSearchFragment
import com.tokopedia.tokopedianow.recipesearch.di.scope.RecipeSearchScope
import com.tokopedia.tokopedianow.recipesearch.presentation.bottomsheet.TokoNowRecipeSearchIngredientBottomSheet
import dagger.Component

@RecipeSearchScope
@Component(
    modules = [
        RecipeSearchModule::class,
        RecipeSearchViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface RecipeSearchComponent {
    fun inject(fragment: TokoNowRecipeSearchFragment)
    fun inject(fragment: TokoNowRecipeSearchIngredientBottomSheet)
}