package com.tokopedia.hotel.booking.di

import com.tokopedia.hotel.booking.presentation.fragment.HotelBookingFragment
import com.tokopedia.hotel.booking.presentation.fragment.HotelContactDataFragment
import com.tokopedia.hotel.booking.presentation.fragment.HotelPayAtHotelPromoFragment
import com.tokopedia.hotel.common.di.component.HotelComponent
import dagger.Component

/**
 * @author by jessica on 15/04/19
 */

@HotelBookingScope
@Component(modules = [HotelBookingModule::class, HotelBookingViewModelModule::class], dependencies = [HotelComponent::class])
interface HotelBookingComponent {

    fun inject(hotelBookingFragment: HotelBookingFragment)

    fun inject(hotelContactDataFragment: HotelContactDataFragment)

    fun inject(hotelPayAtHotelPromoFragment: HotelPayAtHotelPromoFragment)

}