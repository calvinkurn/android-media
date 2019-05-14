package com.tokopedia.hotel.roomdetail.di

import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.roomdetail.presentation.fragment.HotelRoomDetailFragment
import dagger.Component

/**
 * @author by resakemal on 23/04/19
 */

@HotelRoomDetailScope
@Component(modules = [HotelRoomDetailModule::class, HotelRoomDetailViewModelModule::class], dependencies = [HotelComponent::class])
interface HotelRoomDetailComponent {

    fun inject(hotelRoomDetailFragment: HotelRoomDetailFragment)

}