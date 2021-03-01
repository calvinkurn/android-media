package com.tokopedia.hotel.search_map.di

import com.tokopedia.hotel.common.di.component.HotelComponent
import dagger.Component

/**
 * @author by furqan on 01/03/2021
 */
@Component(modules = [HotelSearchMapModule::class, HotelSearchMapViewModelModule::class],
        dependencies = [HotelComponent::class])
@HotelSearchMapScope
interface HotelSearchMapComponent {
}