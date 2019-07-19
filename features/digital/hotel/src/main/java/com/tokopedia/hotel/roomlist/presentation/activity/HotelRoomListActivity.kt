package com.tokopedia.hotel.roomlist.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.roomlist.di.DaggerHotelRoomListComponent
import com.tokopedia.hotel.roomlist.di.HotelRoomListComponent
import com.tokopedia.hotel.roomlist.presentation.fragment.HotelRoomListFragment

class HotelRoomListActivity : HotelBaseActivity(), HasComponent<HotelRoomListComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.contentInsetStartWithNavigation = 0
        supportActionBar?.elevation = 0.3f
    }

    override fun getComponent(): HotelRoomListComponent =
            DaggerHotelRoomListComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()

    override fun getNewFragment(): Fragment = HotelRoomListFragment.createInstance(
            intent.getIntExtra(HotelRoomListFragment.ARG_PROPERTY_ID, 0),
            intent.getStringExtra(HotelRoomListFragment.ARG_PROPERTY_NAME),
            intent.getStringExtra(HotelRoomListFragment.ARG_CHECK_IN),
            intent.getStringExtra(HotelRoomListFragment.ARG_CHECK_OUT),
            intent.getIntExtra(HotelRoomListFragment.ARG_TOTAL_ADULT, 0),
            intent.getIntExtra(HotelRoomListFragment.ARG_TOTAL_CHILDREN, 0),
            intent.getIntExtra(HotelRoomListFragment.ARG_TOTAL_ROOM, 0))

    override fun shouldShowOptionMenu(): Boolean = false

    companion object {
        fun createInstance(context: Context, propertyId: Int = 0, propertyName: String = "", checkIn: String = "", checkOut: String = "",
                           totalAdult: Int = 0, totalChildren: Int = 0, totalRoom: Int = 0): Intent =
                Intent(context, HotelRoomListActivity::class.java)
                        .putExtra(HotelRoomListFragment.ARG_PROPERTY_ID, propertyId)
                        .putExtra(HotelRoomListFragment.ARG_PROPERTY_NAME, propertyName)
                        .putExtra(HotelRoomListFragment.ARG_CHECK_IN, checkIn)
                        .putExtra(HotelRoomListFragment.ARG_CHECK_OUT, checkOut)
                        .putExtra(HotelRoomListFragment.ARG_TOTAL_ADULT, totalAdult)
                        .putExtra(HotelRoomListFragment.ARG_TOTAL_CHILDREN, totalChildren)
                        .putExtra(HotelRoomListFragment.ARG_TOTAL_ROOM, totalRoom)
    }
}