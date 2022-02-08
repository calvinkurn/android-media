package com.tokopedia.gifting.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

@GiftingScope
@Component(modules = [GiftingModule::class, GiftingViewModelModule::class], dependencies = [BaseAppComponent::class])
interface GiftingComponent {

    //fun inject(productBundleActivity: ProductBundleActivity)
}