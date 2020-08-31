package com.tokopedia.brandlist.brandlist_search.di

import com.tokopedia.brandlist.brandlist_search.presentation.fragment.BrandlistSearchFragment
import com.tokopedia.brandlist.common.BrandlistDispatcherProvider
import com.tokopedia.brandlist.common.di.BrandlistComponent
import dagger.Component


@BrandlistSearchScope
@Component(modules = [BrandlistSearchModule::class], dependencies = [BrandlistComponent::class])
interface BrandlistSearchComponent {
    fun inject(view: BrandlistSearchFragment)
    fun getDispatcherProvider(): BrandlistDispatcherProvider
}