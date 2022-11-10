package com.tokopedia.tokopedianow.recipehome.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.recipehome.di.module.RecipeHomeModule
import com.tokopedia.tokopedianow.recipehome.di.module.RecipeHomeViewModelModule
import com.tokopedia.tokopedianow.recipehome.di.scope.RecipeHomeScope
import com.tokopedia.tokopedianow.recipehome.presentation.fragment.TokoNowRecipeHomeFragment
import dagger.Component

@RecipeHomeScope
@Component(
    modules = [
        RecipeHomeModule::class,
        RecipeHomeViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface RecipeHomeComponent {

    fun inject(fragment: TokoNowRecipeHomeFragment)
}