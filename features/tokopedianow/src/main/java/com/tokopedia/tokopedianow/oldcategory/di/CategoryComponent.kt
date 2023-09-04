package com.tokopedia.tokopedianow.oldcategory.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.oldcategory.presentation.view.TokoNowCategoryFragment
import com.tokopedia.tokopedianow.searchcategory.di.UserSessionModule
import dagger.Component

@CategoryScope
@Component(
        modules = [
            CategoryViewModelModule::class,
            CategoryContextModule::class,
            CategoryParamModule::class,
            UserSessionModule::class,
                  ],
        dependencies = [BaseAppComponent::class])
interface CategoryComponent {

    fun inject(tokoNowCategoryFragment: TokoNowCategoryFragment)
}
