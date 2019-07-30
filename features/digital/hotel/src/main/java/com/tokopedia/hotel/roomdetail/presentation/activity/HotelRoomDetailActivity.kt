package com.tokopedia.hotel.roomdetail.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.roomdetail.di.DaggerHotelRoomDetailComponent
import com.tokopedia.hotel.roomdetail.di.HotelRoomDetailComponent
import com.tokopedia.hotel.roomdetail.presentation.fragment.HotelRoomDetailFragment

/**
 * @author by resakemal on 23/04/19
 */

class HotelRoomDetailActivity : HotelBaseActivity(), HasComponent<HotelRoomDetailComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun getComponent(): HotelRoomDetailComponent =
            DaggerHotelRoomDetailComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()

    override fun getNewFragment(): Fragment =
        HotelRoomDetailFragment.getInstance(
                intent.getStringExtra(EXTRA_SAVED_INSTANCE_ID),
                intent.getIntExtra(EXTRA_ROOM_INDEX, 0)
        )

    override fun shouldShowOptionMenu(): Boolean = false

    companion object {

        const val EXTRA_SAVED_INSTANCE_ID = "EXTRA_SAVED_INSTANCE_ID"
        const val EXTRA_ROOM_INDEX = "EXTRA_ROOM_INDEX"

        fun getCallingIntent(context: Context, savedInstanceId: String, roomIndex: Int): Intent =
                Intent(context, HotelRoomDetailActivity::class.java)
                        .putExtra(EXTRA_SAVED_INSTANCE_ID, savedInstanceId)
                        .putExtra(EXTRA_ROOM_INDEX, roomIndex)
    }
}