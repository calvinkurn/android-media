package com.tokopedia.category.navbottomsheet.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.category.navbottomsheet.view.CategoryNavBottomSheet
import dagger.Component

@CategoryNavBottomSheetScope
@Component(modules = [ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface CategoryNavigationBottomSheetComponent {
    fun inject(categoryNavBottom: CategoryNavBottomSheet)
}