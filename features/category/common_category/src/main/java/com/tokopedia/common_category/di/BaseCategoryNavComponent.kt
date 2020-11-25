package com.tokopedia.common_category.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.common_category.fragment.BaseBannedProductFragment
import dagger.Component

@BaseCategoryNavScope
@Component(modules = [BaseCategoryNavUseCaseModule::class,
    BaseCategoryViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface BaseCategoryNavComponent {
    fun inject(bannedProductFragment: BaseBannedProductFragment)
}