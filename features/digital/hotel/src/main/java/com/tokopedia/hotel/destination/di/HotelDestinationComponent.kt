package com.tokopedia.hotel.destination.di

import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.destination.view.fragment.HotelRecommendationFragment
import com.tokopedia.hotel.destination.view.fragment.HotelSearchDestinationFragment
import dagger.Component

/**
 * @author by jessica on 26/03/19
 */

@HotelDestinationScope
@Component(modules = [HotelDestinationModule::class, HotelDestinationViewModelModule::class],
        dependencies = [HotelComponent::class])
interface HotelDestinationComponent {

    fun inject(hotelDestinationActivity: HotelDestinationActivity)

    fun inject(hotelRecommendationFragment: HotelRecommendationFragment)

    fun inject(hotelSearchDestinationFragment: HotelSearchDestinationFragment)

}