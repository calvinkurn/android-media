package com.tokopedia.sellerorder.list.di

import com.tokopedia.sellerorder.common.di.SomComponent
import com.tokopedia.sellerorder.list.presentation.fragments.SomListFragment
import dagger.Component

@SomListScope
@Component(modules = [SomListViewModelModule::class], dependencies = [SomComponent::class])
interface SomListComponent {
    fun inject(fragment: SomListFragment)
}