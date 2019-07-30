package com.tokopedia.hotel.search.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.search.di.DaggerHotelSearchPropertyComponent
import com.tokopedia.hotel.search.di.HotelSearchPropertyComponent
import com.tokopedia.hotel.search.presentation.fragment.HotelSearchResultFragment

class HotelSearchResultActivity: HotelBaseActivity(), HasComponent<HotelSearchPropertyComponent> {
    override fun shouldShowOptionMenu(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val checkIn = TravelDateUtil.dateToString(TravelDateUtil.VIEW_FORMAT_WITHOUT_YEAR, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, intent.getStringExtra(HotelSearchResultFragment.ARG_CHECK_IN)))
        val checkOut = TravelDateUtil.dateToString(TravelDateUtil.VIEW_FORMAT_WITHOUT_YEAR, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, intent.getStringExtra(HotelSearchResultFragment.ARG_CHECK_OUT)))
        val subtitle = getString(R.string.template_search_subtitle,
                checkIn,
                checkOut,
                intent.getIntExtra(HotelSearchResultFragment.ARG_TOTAL_ROOM, 1),
                intent.getIntExtra(HotelSearchResultFragment.ARG_TOTAL_ADULT, 1))
        updateTitle(intent.getStringExtra(HotelSearchResultFragment.ARG_DESTINATION_NAME), subtitle)
    }

    override fun getNewFragment(): Fragment {
        return HotelSearchResultFragment.createInstance(intent.getStringExtra(HotelSearchResultFragment.ARG_DESTINATION_NAME),
                intent.getIntExtra(HotelSearchResultFragment.ARG_DESTINATION_ID, 0),
                intent.getStringExtra(HotelSearchResultFragment.ARG_TYPE),
                intent.getFloatExtra(HotelSearchResultFragment.ARG_LAT, 0f),
                intent.getFloatExtra(HotelSearchResultFragment.ARG_LONG, 0f),
                intent.getStringExtra(HotelSearchResultFragment.ARG_CHECK_IN),
                intent.getStringExtra(HotelSearchResultFragment.ARG_CHECK_OUT),
                intent.getIntExtra(HotelSearchResultFragment.ARG_TOTAL_ROOM, 1),
                intent.getIntExtra(HotelSearchResultFragment.ARG_TOTAL_ADULT, 1),
                intent.getIntExtra(HotelSearchResultFragment.ARG_TOTAL_CHILDREN, 0))
    }

    override fun getComponent(): HotelSearchPropertyComponent =
            DaggerHotelSearchPropertyComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()


    companion object {
        fun createIntent(context: Context, destinationName: String = "", destinationID: Int = 0, type: String = "",
                         latitude: Float = 0f, longitude: Float = 0f, checkIn: String = "",
                         checkOut: String = "", totalRoom: Int = 1, totalAdult: Int = 0,
                         totalChildren: Int = 0): Intent =
                Intent(context, HotelSearchResultActivity::class.java)
                        .putExtra(HotelSearchResultFragment.ARG_DESTINATION_NAME, destinationName)
                        .putExtra(HotelSearchResultFragment.ARG_DESTINATION_ID, destinationID)
                        .putExtra(HotelSearchResultFragment.ARG_TYPE, type)
                        .putExtra(HotelSearchResultFragment.ARG_LAT, latitude)
                        .putExtra(HotelSearchResultFragment.ARG_LONG, longitude)
                        .putExtra(HotelSearchResultFragment.ARG_CHECK_IN, checkIn)
                        .putExtra(HotelSearchResultFragment.ARG_CHECK_OUT, checkOut)
                        .putExtra(HotelSearchResultFragment.ARG_TOTAL_ROOM, totalRoom)
                        .putExtra(HotelSearchResultFragment.ARG_TOTAL_ADULT, totalAdult)
                        .putExtra(HotelSearchResultFragment.ARG_TOTAL_CHILDREN, totalChildren)
    }
}