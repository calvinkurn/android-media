package com.tokopedia.tokopedianow.recipedetail.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.recipedetail.di.module.RecipeDetailModule
import com.tokopedia.tokopedianow.recipedetail.di.module.RecipeDetailViewModelModule
import com.tokopedia.tokopedianow.recipedetail.di.scope.RecipeDetailScope
import com.tokopedia.tokopedianow.recipedetail.presentation.fragment.TokoNowRecipeDetailFragment
import com.tokopedia.tokopedianow.recipedetail.presentation.fragment.TokoNowRecipeIngredientFragment
import com.tokopedia.tokopedianow.recipedetail.presentation.fragment.TokoNowRecipeInstructionFragment
import com.tokopedia.tokopedianow.recipedetail.presentation.fragment.TokoNowRecipeSimilarProductFragment
import dagger.Component

@RecipeDetailScope
@Component(
    modules = [
        RecipeDetailModule::class,
        RecipeDetailViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface RecipeDetailComponent {

    fun inject(fragment: TokoNowRecipeDetailFragment)
    fun inject(fragment: TokoNowRecipeInstructionFragment)
    fun inject(fragment: TokoNowRecipeIngredientFragment)
    fun inject(fragment: TokoNowRecipeSimilarProductFragment)
}