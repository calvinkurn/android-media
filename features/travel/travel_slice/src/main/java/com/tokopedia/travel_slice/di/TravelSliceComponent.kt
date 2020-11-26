package com.tokopedia.travel_slice.di

import com.tokopedia.travel_slice.ui.provider.MainSliceProvider
import dagger.Component

@TravelSliceScope
@Component(modules = [TravelSliceModule::class])
interface TravelSliceComponent {
    fun inject(mainSliceProvider: MainSliceProvider)
}