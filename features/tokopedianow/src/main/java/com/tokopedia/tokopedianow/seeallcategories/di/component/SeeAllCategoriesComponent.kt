package com.tokopedia.tokopedianow.seeallcategories.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.seeallcategories.di.module.SeeAllCategoriesModule
import com.tokopedia.tokopedianow.seeallcategories.di.module.SeeAllCategoriesUseCaseModule
import com.tokopedia.tokopedianow.seeallcategories.di.module.SeeAllCategoriesViewModelModule
import com.tokopedia.tokopedianow.seeallcategories.di.scope.SeeAllCategoriesScope
import com.tokopedia.tokopedianow.seeallcategories.persentation.fragment.TokoNowSeeAllCategoriesFragment
import dagger.Component

@SeeAllCategoriesScope
@Component(
    modules = [
        SeeAllCategoriesModule::class,
        SeeAllCategoriesViewModelModule::class,
        SeeAllCategoriesUseCaseModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface SeeAllCategoriesComponent {
    fun inject(fragment: TokoNowSeeAllCategoriesFragment)
}
