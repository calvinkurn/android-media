package com.tokopedia.tokopedianow.seeallcategory.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.seeallcategory.di.module.SeeAllCategoryModule
import com.tokopedia.tokopedianow.seeallcategory.di.module.SeeAllCategoryUseCaseModule
import com.tokopedia.tokopedianow.seeallcategory.di.module.SeeAllCategoryViewModelModule
import com.tokopedia.tokopedianow.seeallcategory.di.scope.SeeAllCategoryScope
import com.tokopedia.tokopedianow.seeallcategory.persentation.fragment.TokoNowSeeAllCategoryFragment
import dagger.Component

@SeeAllCategoryScope
@Component(
    modules = [
        SeeAllCategoryModule::class,
        SeeAllCategoryViewModelModule::class,
        SeeAllCategoryUseCaseModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface SeeAllCategoryComponent {
    fun inject(fragment: TokoNowSeeAllCategoryFragment)
}
