package com.tokopedia.tokopedianow.recipebookmark.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.recipebookmark.di.module.RecipeBookmarkModule
import com.tokopedia.tokopedianow.recipebookmark.di.module.RecipeBookmarkViewModelModule
import com.tokopedia.tokopedianow.recipebookmark.di.scope.RecipeBookmarkScope
import com.tokopedia.tokopedianow.recipebookmark.presentation.activity.TokoNowRecipeBookmarkActivity
import com.tokopedia.tokopedianow.oldrecipebookmark.presentation.fragment.TokoNowRecipeBookmarkFragment
import dagger.Component

@RecipeBookmarkScope
@Component(
    modules = [
        RecipeBookmarkModule::class,
        RecipeBookmarkViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface RecipeBookmarkComponent {

    fun inject(activity: TokoNowRecipeBookmarkActivity)
    fun inject(fragment: TokoNowRecipeBookmarkFragment)
}
