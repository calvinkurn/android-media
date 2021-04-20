package com.tokopedia.sellerorder.filter.di

import com.tokopedia.sellerorder.common.di.SomComponent
import com.tokopedia.sellerorder.filter.presentation.bottomsheet.SomFilterBottomSheet
import dagger.Component

@SomFilterScope
@Component(modules = [SomFilterViewModelModule::class], dependencies = [SomComponent::class])
interface SomFilterComponent {
    fun inject(fragment: SomFilterBottomSheet)
}