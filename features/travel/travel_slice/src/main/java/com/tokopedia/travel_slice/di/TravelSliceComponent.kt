package com.tokopedia.travel_slice.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.travel_slice.ui.provider.MainSliceProvider
import com.tokopedia.travel_slice.ui.provider.TravelSliceActivity
import dagger.Component

@TravelSliceScope
@Component(modules = [TravelSliceModule::class],
        dependencies = [BaseAppComponent::class])
interface TravelSliceComponent {
    fun inject(mainSliceProvider: MainSliceProvider)
    fun inject(activity: TravelSliceActivity)
}