package com.tokopedia.hotel.search.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.search.di.DaggerHotelSearchPropertyComponent
import com.tokopedia.hotel.search.di.HotelSearchPropertyComponent
import com.tokopedia.hotel.search.presentation.fragment.HotelSearchResultFragment
import java.util.*

class HotelSearchResultActivity : HotelBaseActivity(), HasComponent<HotelSearchPropertyComponent> {

    var checkIn = ""
    var checkOut = ""
    var checkInString = ""
    var checkOutString = ""
    var id = 0
    var name = ""
    var type = ""
    var room = 1
    var adult = 1
    var lat = 0f
    var long = 0f
    var children = 0
    var subtitle = ""

    override fun getParentViewResourceID() = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

    override fun shouldShowOptionMenu(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        val uri = intent.data
        if (uri != null) {
            if (!uri.getQueryParameter(PARAM_HOTEL_ID).isNullOrEmpty()) {
                id = uri.getQueryParameter(PARAM_HOTEL_ID).toInt()
                name = uri.getQueryParameter(PARAM_HOTEL_NAME)
                type = TYPE_PROPERTY
            } else if (!uri.getQueryParameter(PARAM_CITY_ID).isNullOrEmpty()) {
                id = uri.getQueryParameter(PARAM_CITY_ID).toInt()
                name = uri.getQueryParameter(PARAM_CITY_NAME)
                type = TYPE_CITY
            } else if (!uri.getQueryParameter(PARAM_DISTRICT_ID).isNullOrEmpty()) {
                id = uri.getQueryParameter(PARAM_DISTRICT_ID).toInt()
                name = uri.getQueryParameter(PARAM_DISTRICT_NAME)
                type = TYPE_DISTRICT
            } else if (!uri.getQueryParameter(PARAM_REGION_ID).isNullOrEmpty()) {
                id = uri.getQueryParameter(PARAM_REGION_ID).toInt()
                name = uri.getQueryParameter(PARAM_REGION_NAME)
                type = TYPE_REGION
            }

            if (!uri.getQueryParameter(PARAM_CHECK_IN).isNullOrEmpty()) checkIn = uri.getQueryParameter(PARAM_CHECK_IN)
            if (!uri.getQueryParameter(PARAM_CHECK_OUT).isNullOrEmpty()) checkOut = uri.getQueryParameter(PARAM_CHECK_OUT)
            if (!uri.getQueryParameter(PARAM_ROOM).isNullOrEmpty()) room = uri.getQueryParameter(PARAM_ROOM).toInt()
            if (!uri.getQueryParameter(PARAM_ADULT).isNullOrEmpty()) adult = uri.getQueryParameter(PARAM_ADULT).toInt()

        } else {
            name = intent.getStringExtra(HotelSearchResultFragment.ARG_DESTINATION_NAME)
            id = intent.getIntExtra(HotelSearchResultFragment.ARG_DESTINATION_ID, 0)
            type = intent.getStringExtra(HotelSearchResultFragment.ARG_TYPE)
            lat = intent.getFloatExtra(HotelSearchResultFragment.ARG_LAT, 0f)
            long = intent.getFloatExtra(HotelSearchResultFragment.ARG_LONG, 0f)
            room = intent.getIntExtra(HotelSearchResultFragment.ARG_TOTAL_ROOM, 1)
            adult = intent.getIntExtra(HotelSearchResultFragment.ARG_TOTAL_ADULT, 1)
            children = intent.getIntExtra(HotelSearchResultFragment.ARG_TOTAL_CHILDREN, 0)
            checkIn = intent.getStringExtra(HotelSearchResultFragment.ARG_CHECK_IN)
            checkOut = intent.getStringExtra(HotelSearchResultFragment.ARG_CHECK_OUT)
        }

        checkParameter()

        super.onCreate(savedInstanceState)

        checkInString = TravelDateUtil.dateToString(TravelDateUtil.VIEW_FORMAT_WITHOUT_YEAR, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, checkIn))
        checkOutString = TravelDateUtil.dateToString(TravelDateUtil.VIEW_FORMAT_WITHOUT_YEAR, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, checkOut))
        subtitle = getString(R.string.template_search_subtitle,
                checkInString,
                checkOutString,
                room,
                adult)
        updateTitle(name, subtitle)

    }

    fun checkParameter() {
        val todayWithoutTime = TravelDateUtil.removeTime(TravelDateUtil.getCurrentCalendar().time)
        val tomorrow = TravelDateUtil.addTimeToSpesificDate(todayWithoutTime, Calendar.DATE, 1)
        val dayAfterTomorrow = TravelDateUtil.addTimeToSpesificDate(todayWithoutTime, Calendar.DATE, 2)

        if (checkIn.isBlank() && checkOut.isBlank()) {
            checkIn = TravelDateUtil.dateToString(
                    TravelDateUtil.YYYY_MM_DD, tomorrow)
            checkInString = TravelDateUtil.dateToString(
                    TravelDateUtil.DEFAULT_VIEW_FORMAT, tomorrow)
            checkOut = TravelDateUtil.dateToString(
                    TravelDateUtil.YYYY_MM_DD, dayAfterTomorrow)
            checkOutString = TravelDateUtil.dateToString(
                    TravelDateUtil.DEFAULT_VIEW_FORMAT, dayAfterTomorrow)
        } else if (checkIn.isBlank()) {
            val checkout = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, checkOut)
            val dayBeforeCheckOut = TravelDateUtil.addTimeToSpesificDate(checkout, Calendar.DATE, -1)
            checkIn = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, dayBeforeCheckOut)
            checkInString = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT, dayBeforeCheckOut)

        } else if (checkOut.isBlank()) {
            val checkin = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, checkIn)
            val dayAfterCheckIn = TravelDateUtil.addTimeToSpesificDate(checkin, Calendar.DATE, 1)
            checkOut = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, dayAfterCheckIn)
            checkOutString = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT, dayAfterCheckIn)
        }

    }

    override fun getNewFragment(): Fragment {
        return HotelSearchResultFragment.createInstance(name, id, type, lat, long, checkIn, checkOut, room, adult, children)
    }

    override fun getComponent(): HotelSearchPropertyComponent =
            DaggerHotelSearchPropertyComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()


    companion object {

        const val PARAM_HOTEL_ID = "hotel_id"
        const val PARAM_HOTEL_NAME = "hotel_name"
        const val PARAM_DISTRICT_ID = "district_id"
        const val PARAM_DISTRICT_NAME = "district_name"
        const val PARAM_CITY_ID = "city_id"
        const val PARAM_CITY_NAME = "city_name"
        const val PARAM_REGION_ID = "region_id"
        const val PARAM_REGION_NAME = "region_name"
        const val PARAM_CHECK_IN = "check_in"
        const val PARAM_CHECK_OUT = "check_out"
        const val PARAM_ROOM = "room"
        const val PARAM_ADULT = "adult"

        const val TYPE_REGION = "region"
        const val TYPE_DISTRICT = "district"
        const val TYPE_CITY = "city"
        const val TYPE_PROPERTY = "property"

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