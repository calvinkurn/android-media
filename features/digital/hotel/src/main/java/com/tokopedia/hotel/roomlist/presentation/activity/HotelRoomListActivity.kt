package com.tokopedia.hotel.roomlist.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.homepage.presentation.fragment.HotelHomepageFragment
import com.tokopedia.hotel.roomlist.di.DaggerHotelRoomListComponent
import com.tokopedia.hotel.roomlist.di.HotelRoomListComponent
import com.tokopedia.hotel.roomlist.presentation.fragment.HotelRoomListFragment

class HotelRoomListActivity : HotelBaseActivity(), HasComponent<HotelRoomListComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.contentInsetStartWithNavigation = 0
    }

    override fun getComponent(): HotelRoomListComponent =
            DaggerHotelRoomListComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()

    override fun getScreenName(): String = ""

    override fun getNewFragment(): Fragment = HotelRoomListFragment()

    override fun shouldShowOptionMenu(): Boolean = true

    companion object {
        fun getCallingIntent(context: Context): Intent =
                Intent(context, HotelRoomListActivity::class.java)
    }
}