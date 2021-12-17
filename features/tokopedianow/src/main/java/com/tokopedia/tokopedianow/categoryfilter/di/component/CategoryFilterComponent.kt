package com.tokopedia.tokopedianow.categoryfilter.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.categoryfilter.di.module.CategoryFilterModule
import com.tokopedia.tokopedianow.categoryfilter.di.module.CategoryFilterViewModelModule
import com.tokopedia.tokopedianow.categoryfilter.di.scope.CategoryFilterScope
import com.tokopedia.tokopedianow.categoryfilter.presentation.bottomsheet.TokoNowCategoryFilterBottomSheet
import dagger.Component

@CategoryFilterScope
@Component(
    modules = [
        CategoryFilterModule::class,
        CategoryFilterViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface CategoryFilterComponent {

    fun inject(bottomSheet: TokoNowCategoryFilterBottomSheet)
}