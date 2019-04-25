package com.tokopedia.hotel.roomdetail.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.roomdetail.di.DaggerHotelRoomDetailComponent
import com.tokopedia.hotel.roomdetail.di.HotelRoomDetailComponent
import com.tokopedia.hotel.roomdetail.view.fragment.HotelRoomDetailFragment

/**
 * @author by resakemal on 23/04/19
 */

class HotelRoomDetailActivity : HotelBaseActivity(), HasComponent<HotelRoomDetailComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle("Kamar Deluxe Double")
    }

    override fun getComponent(): HotelRoomDetailComponent =
            DaggerHotelRoomDetailComponent.builder()
                    .hotelComponent(getHotelComponent())
                    .build()

    override fun getScreenName(): String = ""

    override fun getNewFragment(): Fragment =
        HotelRoomDetailFragment.getInstance()

    override fun shouldShowOptionMenu(): Boolean = false

    companion object {
        fun getCallingIntent(context: Context): Intent =
                Intent(context, HotelRoomDetailActivity::class.java)
    }
}