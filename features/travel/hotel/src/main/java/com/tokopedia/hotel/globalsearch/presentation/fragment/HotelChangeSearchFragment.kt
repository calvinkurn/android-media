package com.tokopedia.hotel.globalsearch.presentation.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.CHECK_IN_DATE
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.CHECK_OUT_DATE
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.DESTINATION_ID
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.DESTINATION_LAT
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.DESTINATION_LONG
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.DESTINATION_NAME
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.DESTINATION_TYPE
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.EXTRA_CHECK_IN_DATE
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.EXTRA_CHECK_OUT_DATE
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.EXTRA_DESTINATION_ID
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.EXTRA_DESTINATION_LAT
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.EXTRA_DESTINATION_LONG
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.EXTRA_DESTINATION_NAME
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.EXTRA_DESTINATION_SEARCH_ID
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.EXTRA_DESTINATION_SEARCH_TYPE
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.EXTRA_DESTINATION_TYPE
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.EXTRA_NUM_OF_GUESTS
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.EXTRA_NUM_OF_ROOMS
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.NUM_OF_GUESTS
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.NUM_OF_ROOMS
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.SEARCH_ID
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity.Companion.SEARCH_TYPE
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_hotel_change_search.*
import java.util.*

/**
 * @author by jessica on 06/04/20
 */

class HotelChangeSearchFragment : HotelGlobalSearchFragment() {

    private val trackingUtil: TrackingHotelUtil by lazy { TrackingHotelUtil() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_change_search, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            globalSearchModel.destinationId = it.getLong(EXTRA_DESTINATION_ID, 0)
            globalSearchModel.destinationName = it.getString(EXTRA_DESTINATION_NAME) ?: ""
            globalSearchModel.destinationType = it.getString(EXTRA_DESTINATION_TYPE) ?: ""
            globalSearchModel.destinationType
            globalSearchModel.checkInDate = it.getString(EXTRA_CHECK_IN_DATE)
                    ?: TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, TravelDateUtil.addTimeToSpesificDate(TravelDateUtil.getCurrentCalendar().time, Calendar.DATE, 1))
            globalSearchModel.checkInDateFmt = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, it.getString(EXTRA_CHECK_IN_DATE)))
            globalSearchModel.checkOutDate = it.getString(EXTRA_CHECK_OUT_DATE)
                    ?: TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, TravelDateUtil.addTimeToSpesificDate(TravelDateUtil.getCurrentCalendar().time, Calendar.DATE, 2))
            globalSearchModel.checkOutDateFmt = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, it.getString(EXTRA_CHECK_OUT_DATE)))
            globalSearchModel.numOfGuests = it.getInt(EXTRA_NUM_OF_GUESTS)
            globalSearchModel.numOfRooms = it.getInt(EXTRA_NUM_OF_ROOMS)
            globalSearchModel.locationLong = it.getDouble(EXTRA_DESTINATION_LONG, 0.0)
            globalSearchModel.locationLat = it.getDouble(EXTRA_DESTINATION_LAT, 0.0)
            globalSearchModel.searchType = it.getString(EXTRA_DESTINATION_SEARCH_TYPE) ?: ""
            globalSearchModel.searchId = it.getString(EXTRA_DESTINATION_SEARCH_ID) ?: ""

            globalSearchModel.nightCount = countRoomDuration()

            renderView()
            trackingUtil.openScreen(context, SCREEN_NAME)
        }
    }

    override fun onCheckAvailabilityClicked() {
        val type: String = if (globalSearchModel.searchType.isNotEmpty()) globalSearchModel.searchType
        else globalSearchModel.destinationType

        trackingUtil.clickSaveChangeSearch(context,
                type,
                globalSearchModel.destinationName,
                globalSearchModel.numOfRooms,
                globalSearchModel.numOfGuests,
                globalSearchModel.checkInDate,
                globalSearchModel.checkOutDate,
                SCREEN_NAME)

        when {
            globalSearchModel.destinationType.equals(HotelTypeEnum.PROPERTY.value, false) -> {
                context?.let {
                    startActivityForResult(HotelDetailActivity.getCallingIntent(it,
                            globalSearchModel.checkInDate,
                            globalSearchModel.checkOutDate,
                            globalSearchModel.destinationId,
                            globalSearchModel.numOfRooms,
                            globalSearchModel.numOfGuests,
                            globalSearchModel.destinationType,
                            globalSearchModel.destinationName),
                            REQUEST_CODE_DETAIL)
                }
            }

            globalSearchModel.searchType.equals(HotelTypeEnum.PROPERTY.value, false) -> {
                context?.let {
                    startActivityForResult(HotelDetailActivity.getCallingIntent(it,
                            globalSearchModel.checkInDate,
                            globalSearchModel.checkOutDate,
                            globalSearchModel.searchId.toLong(),
                            globalSearchModel.numOfRooms,
                            globalSearchModel.numOfGuests,
                            globalSearchModel.searchType,
                            globalSearchModel.destinationName),
                            REQUEST_CODE_DETAIL)
                }
            }

            else -> {
                val intent = Intent().apply {
                    putExtra(DESTINATION_ID, globalSearchModel.destinationId)
                    putExtra(DESTINATION_TYPE, globalSearchModel.destinationType)
                    putExtra(DESTINATION_NAME,  globalSearchModel.destinationName)
                    putExtra(DESTINATION_LAT, globalSearchModel.locationLat)
                    putExtra(DESTINATION_LONG, globalSearchModel.locationLong)
                    putExtra(CHECK_IN_DATE, globalSearchModel.checkInDate)
                    putExtra(CHECK_OUT_DATE, globalSearchModel.checkOutDate)
                    putExtra(NUM_OF_GUESTS, globalSearchModel.numOfGuests)
                    putExtra(NUM_OF_ROOMS, globalSearchModel.numOfRooms)
                    putExtra(SEARCH_TYPE, globalSearchModel.searchType)
                    putExtra(SEARCH_ID, globalSearchModel.searchId)
                }
                activity?.setResult(RESULT_OK, intent)
                activity?.finish()
            }
        }

    }

    override fun renderView() {
        super.renderView()
        tv_hotel_homepage_destination.setText(globalSearchModel.destinationName)
        tv_hotel_homepage_destination.setOnClickListener { onDestinationChangeClicked() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_DESTINATION -> if (resultCode == RESULT_OK && data != null) {
                when {
                    data.hasExtra(HotelDestinationActivity.HOTEL_CURRENT_LOCATION_LANG) -> {
                        onDestinationNearBy(data.getDoubleExtra(HotelDestinationActivity.HOTEL_CURRENT_LOCATION_LANG, 0.0),
                                data.getDoubleExtra(HotelDestinationActivity.HOTEL_CURRENT_LOCATION_LAT, 0.0))
                    }
                    data.hasExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_ID) -> {
                        onDestinationChanged(data.getStringExtra(HotelDestinationActivity.HOTEL_DESTINATION_NAME),
                                searchId = data.getStringExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_ID) ?: "",
                                searchType = data.getStringExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_TYPE) ?: "")
                    }
                }
            }
        }
    }

    private fun onDestinationChangeClicked() {
        context?.run {
            startActivityForResult(HotelDestinationActivity.createInstance(this), REQUEST_CODE_DESTINATION)
        }
        activity?.overridePendingTransition(com.tokopedia.common.travel.R.anim.travel_slide_up_in,
                com.tokopedia.common.travel.R.anim.travel_anim_stay)
    }

    private fun onDestinationNearBy(longitude: Double, latitude: Double) {
        globalSearchModel.destinationName = getString(R.string.hotel_homepage_near_by_destination)
        globalSearchModel.locationLat = latitude
        globalSearchModel.locationLong = longitude
        globalSearchModel.destinationId = 0
        globalSearchModel.destinationType = ""
        globalSearchModel.searchId = ""
        globalSearchModel.searchType = HotelTypeEnum.COORDINATE.value
        renderView()
    }

    private fun onDestinationChanged(name: String, destinationId: Long = 0, type: String  = "",
                                     searchId: String = "", searchType: String = "") {
        globalSearchModel.destinationName = name
        globalSearchModel.destinationId = destinationId
        globalSearchModel.destinationType = type
        globalSearchModel.locationLat = 0.0
        globalSearchModel.locationLong = 0.0
        globalSearchModel.searchType = searchType
        globalSearchModel.searchId =  searchId
        renderView()
    }

    companion object {

        const val REQUEST_CODE_DESTINATION = 101
        const val REQUEST_CODE_DETAIL = 103
        const val SCREEN_NAME = "/hotel/changesearch"

        fun getInstance(destinationId: Long, destinationName: String, destinationType: String, latitude: Double, longitude: Double,
                        checkInDate: String, checkOutDate: String, numOfGuests: Int, numOfRooms: Int, searchId: String, searchType: String) =
                HotelChangeSearchFragment().also {
                    it.arguments = Bundle().apply {
                        putLong(EXTRA_DESTINATION_ID, destinationId)
                        putString(EXTRA_DESTINATION_NAME, destinationName)
                        putString(EXTRA_DESTINATION_TYPE, destinationType)
                        putDouble(EXTRA_DESTINATION_LAT, latitude)
                        putDouble(EXTRA_DESTINATION_LONG, longitude)
                        putString(EXTRA_CHECK_IN_DATE, checkInDate)
                        putString(EXTRA_CHECK_OUT_DATE, checkOutDate)
                        putInt(EXTRA_NUM_OF_GUESTS, numOfGuests)
                        putInt(EXTRA_NUM_OF_ROOMS, numOfRooms)
                        putString(EXTRA_DESTINATION_SEARCH_TYPE, searchType)
                        putString(EXTRA_DESTINATION_SEARCH_ID, searchId)
                    }
                }
    }
}