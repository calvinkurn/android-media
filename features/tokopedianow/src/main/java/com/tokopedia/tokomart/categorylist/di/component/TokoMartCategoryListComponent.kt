package com.tokopedia.tokomart.categorylist.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokomart.categorylist.di.module.TokoMartCategoryListModule
import com.tokopedia.tokomart.categorylist.di.module.TokoMartCategoryListUseCaseModule
import com.tokopedia.tokomart.categorylist.di.module.TokoMartCategoryListViewModelModule
import com.tokopedia.tokomart.categorylist.di.scope.TokoMartCategoryListScope
import com.tokopedia.tokomart.categorylist.presentation.bottomsheet.TokoMartCategoryListBottomSheet
import dagger.Component

@TokoMartCategoryListScope
@Component(
    modules = [
        TokoMartCategoryListModule::class,
        TokoMartCategoryListUseCaseModule::class,
        TokoMartCategoryListViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface TokoMartCategoryListComponent {

    fun inject(fragment: TokoMartCategoryListBottomSheet)
}