package com.tokopedia.tokopedianow.recipeautocomplete.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.recipeautocomplete.di.module.RecipeAutoCompleteModule
import com.tokopedia.tokopedianow.recipeautocomplete.di.module.RecipeAutoCompleteViewModelModule
import com.tokopedia.tokopedianow.recipeautocomplete.di.scope.RecipeAutoCompleteScope
import com.tokopedia.tokopedianow.recipeautocomplete.presentation.fragment.TokoNowRecipeAutoCompleteFragment
import dagger.Component

@RecipeAutoCompleteScope
@Component(
    modules = [
        RecipeAutoCompleteModule::class,
        RecipeAutoCompleteViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface RecipeAutoCompleteComponent {
    fun inject(fragment: TokoNowRecipeAutoCompleteFragment)
}