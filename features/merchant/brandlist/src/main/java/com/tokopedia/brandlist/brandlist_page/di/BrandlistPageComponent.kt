package com.tokopedia.brandlist.brandlist_page.di

import com.tokopedia.brandlist.brandlist_page.presentation.fragment.BrandlistPageFragment
import com.tokopedia.brandlist.common.BrandlistDispatcherProvider
import com.tokopedia.brandlist.common.di.BrandlistComponent
import dagger.Component

@BrandlistPageScope
@Component(modules = [BrandlistPageModule::class], dependencies = [BrandlistComponent::class])
interface BrandlistPageComponent {
    fun inject(view: BrandlistPageFragment)
    fun getDispatcherProvider(): BrandlistDispatcherProvider
}