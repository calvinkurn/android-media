package com.tokopedia.brandlist.brandlist_category.di

import com.tokopedia.brandlist.brandlist_category.presentation.fragment.BrandlistContainerFragment
import com.tokopedia.brandlist.common.BrandlistDispatcherProvider
import com.tokopedia.brandlist.common.di.BrandlistComponent
import dagger.Component

@BrandlistCategoryScope
@Component(modules = [BrandlistCategoryModule::class], dependencies = [BrandlistComponent::class])
interface BrandlistCategoryComponent {
    fun inject(view: BrandlistContainerFragment)
    fun getDispatcherProvider(): BrandlistDispatcherProvider
}