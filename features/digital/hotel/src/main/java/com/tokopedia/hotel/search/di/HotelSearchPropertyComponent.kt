package com.tokopedia.hotel.search.di

import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.search.presentation.fragment.HotelSearchResultFragment
import dagger.Component

@Component(modules = [HotelSearchPropertyModule::class, HotelSearchPropertyVMModule::class],
        dependencies = [HotelComponent::class])
@HotelSearchPropertyScope
interface HotelSearchPropertyComponent{

    fun inject(fragment: HotelSearchResultFragment)
}