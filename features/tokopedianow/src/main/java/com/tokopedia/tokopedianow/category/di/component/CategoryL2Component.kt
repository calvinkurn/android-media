package com.tokopedia.tokopedianow.category.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.category.di.module.CategoryL2ContextModule
import com.tokopedia.tokopedianow.category.di.module.CategoryL2ViewModelModule
import com.tokopedia.tokopedianow.category.di.scope.CategoryL2Scope
import com.tokopedia.tokopedianow.category.presentation.fragment.TokoNowCategoryL2Fragment
import com.tokopedia.tokopedianow.searchcategory.di.UserSessionModule
import dagger.Component

@CategoryL2Scope
@Component(
    modules = [
        CategoryL2ViewModelModule::class,
        CategoryL2ContextModule::class,
        UserSessionModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface CategoryL2Component {
    fun inject(fragment: TokoNowCategoryL2Fragment)
}
