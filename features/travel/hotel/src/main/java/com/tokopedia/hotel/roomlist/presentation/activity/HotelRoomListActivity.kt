package com.tokopedia.hotel.roomlist.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
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
        supportActionBar?.elevation = ELEVATION_ACTION_BAR
    }

    override fun getComponent(): HotelRoomListComponent =
            DaggerHotelRoomListComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()

    override fun getNewFragment(): Fragment = HotelRoomListFragment.createInstance(
            intent.getLongExtra(HotelRoomListFragment.ARG_PROPERTY_ID, 0),
            intent.getStringExtra(HotelRoomListFragment.ARG_PROPERTY_NAME) ?: "",
            intent.getStringExtra(HotelRoomListFragment.ARG_CHECK_IN) ?: "",
            intent.getStringExtra(HotelRoomListFragment.ARG_CHECK_OUT) ?: "",
            intent.getIntExtra(HotelRoomListFragment.ARG_TOTAL_ADULT, 0),
            intent.getIntExtra(HotelRoomListFragment.ARG_TOTAL_CHILDREN, 0),
            intent.getIntExtra(HotelRoomListFragment.ARG_TOTAL_ROOM, 0),
            intent.getStringExtra(HotelRoomListFragment.ARG_DESTINATION_TYPE) ?: "",
            intent.getStringExtra(HotelRoomListFragment.ARG_DESTINATION_NAME) ?: "")

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getScreenName(): String = ROOM_LIST_SCREEN_NAME

    companion object {

        private const val ELEVATION_ACTION_BAR = 0.3f
        const val ROOM_LIST_SCREEN_NAME = "/hotel/roomlist"

        fun createInstance(context: Context, propertyId: Long = 0, propertyName: String = "", checkIn: String = "", checkOut: String = "",
                           totalAdult: Int = 0, totalChildren: Int = 0, totalRoom: Int = 0, destinationType: String, destinationName: String): Intent =
                Intent(context, HotelRoomListActivity::class.java)
                        .putExtra(HotelRoomListFragment.ARG_PROPERTY_ID, propertyId)
                        .putExtra(HotelRoomListFragment.ARG_PROPERTY_NAME, propertyName)
                        .putExtra(HotelRoomListFragment.ARG_CHECK_IN, checkIn)
                        .putExtra(HotelRoomListFragment.ARG_CHECK_OUT, checkOut)
                        .putExtra(HotelRoomListFragment.ARG_TOTAL_ADULT, totalAdult)
                        .putExtra(HotelRoomListFragment.ARG_TOTAL_CHILDREN, totalChildren)
                        .putExtra(HotelRoomListFragment.ARG_TOTAL_ROOM, totalRoom)
                        .putExtra(HotelRoomListFragment.ARG_DESTINATION_TYPE, destinationType)
                        .putExtra(HotelRoomListFragment.ARG_DESTINATION_NAME, destinationName)
    }
}