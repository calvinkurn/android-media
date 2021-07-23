package com.tokopedia.tokopedianow.categorylist.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.categorylist.di.module.CategoryListModule
import com.tokopedia.tokopedianow.categorylist.di.module.CategoryListUseCaseModule
import com.tokopedia.tokopedianow.categorylist.di.module.CategoryListViewModelModule
import com.tokopedia.tokopedianow.categorylist.di.scope.CategoryListScope
import com.tokopedia.tokopedianow.categorylist.presentation.bottomsheet.TokoNowCategoryListBottomSheet
import dagger.Component

@CategoryListScope
@Component(
    modules = [
        CategoryListModule::class,
        CategoryListUseCaseModule::class,
        CategoryListViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface CategoryListComponent {

    fun inject(fragment: TokoNowCategoryListBottomSheet)
}