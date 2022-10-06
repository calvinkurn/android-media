package com.tokopedia.tokopedianow.recipelist.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.recipelist.di.module.RecipeListModule
import com.tokopedia.tokopedianow.recipelist.di.module.RecipeListViewModelModule
import com.tokopedia.tokopedianow.recipelist.di.scope.RecipeListScope
import com.tokopedia.tokopedianow.recipelist.presentation.fragment.TokoNowRecipeFilterFragment
import dagger.Component

@RecipeListScope
@Component(
    modules = [
        RecipeListModule::class,
        RecipeListViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface RecipeListComponent {

    fun inject(fragment: TokoNowRecipeFilterFragment)
}