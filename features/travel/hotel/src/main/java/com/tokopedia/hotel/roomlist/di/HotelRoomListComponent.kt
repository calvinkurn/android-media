package com.tokopedia.hotel.roomlist.di

import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.roomlist.presentation.activity.HotelRoomListActivity
import com.tokopedia.hotel.roomlist.presentation.fragment.HotelRoomListFragment
import dagger.Component

/**
 * @author by jessica on 15/04/19
 */

@HotelRoomListScope
@Component(modules = [HotelRoomListModule::class, HotelRoomListViewModelModule::class], dependencies = [HotelComponent::class])
interface HotelRoomListComponent {

    fun inject(hotelRoomListActivity: HotelRoomListActivity)

    fun inject(hotelRoomListFragment: HotelRoomListFragment)
}