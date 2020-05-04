package com.tokopedia.hotel.cancellation.di

import com.tokopedia.hotel.cancellation.presentation.fragment.HotelCancellationFragment
import com.tokopedia.hotel.common.di.component.HotelComponent
import dagger.Component

/**
 * @author by jessica on 27/04/20
 */

@HotelCancellationScope
@Component(modules = [HotelCancellationModule::class, HotelCancellationViewModelModule::class], dependencies = [HotelComponent::class])
interface HotelCancellationComponent {

    fun injectl(hotelCancellationFragment: HotelCancellationFragment)
}