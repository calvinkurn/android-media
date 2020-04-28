package com.tokopedia.hotel.globalsearch.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.hotel.globalsearch.presentation.fragment.HotelChangeSearchFragment

/**
 * @author by jessica on 06/04/20
 */

class HotelChangeSearchActivity : HotelGlobalSearchActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateTitle(intent.getStringExtra(EXTRA_TITLE))
    }

    override fun getNewFragment(): Fragment {
        return HotelChangeSearchFragment.getInstance(intent.getLongExtra(EXTRA_DESTINATION_ID, 0),
                intent.getStringExtra(EXTRA_DESTINATION_NAME) ?: "",
                intent.getStringExtra(EXTRA_DESTINATION_TYPE) ?: "",
                intent.getDoubleExtra(EXTRA_DESTINATION_LAT, 0.0),
                intent.getDoubleExtra(EXTRA_DESTINATION_LONG, 0.0),
                intent.getStringExtra(EXTRA_CHECK_IN_DATE)  ?: "",
                intent.getStringExtra(EXTRA_CHECK_OUT_DATE) ?: "",
                intent.getIntExtra(EXTRA_NUM_OF_GUESTS, 0),
                intent.getIntExtra(EXTRA_NUM_OF_ROOMS, 0),
                intent.getStringExtra(EXTRA_DESTINATION_SEARCH_ID) ?: "",
                intent.getStringExtra(EXTRA_DESTINATION_SEARCH_TYPE) ?: "")
    }

    override fun getScreenName(): String = ""

    companion object {
        const val EXTRA_CHECK_IN_DATE = "EXTRA_CHECK_IN_DATE"
        const val EXTRA_CHECK_OUT_DATE = "EXTRA_CHECK_OUT_DATE"
        const val EXTRA_NUM_OF_GUESTS = "EXTRA_NUM_OF_GUESTS"
        const val EXTRA_NUM_OF_ROOMS = "EXTRA_NUM_OF_ROOMS"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_DESTINATION_ID = "EXTRA_DESTINATION_ID"
        const val EXTRA_DESTINATION_TYPE = "EXTRA_DESTINATION_TYPE"
        const val EXTRA_DESTINATION_NAME = "EXTRA_DESTINATION_NAME"
        const val EXTRA_DESTINATION_LONG = "EXTRA_DESTINATION_LONG"
        const val EXTRA_DESTINATION_LAT = "EXTRA_DESTINATION_LAT"
        const val EXTRA_DESTINATION_SEARCH_ID = "EXTRA_DESTINATION_SEARCH_ID"
        const val EXTRA_DESTINATION_SEARCH_TYPE = "EXTRA_DESTINATION_SEARCH_TYPE"

        const val DESTINATION_ID = "HOTEL_DESTINATION_ID"
        const val DESTINATION_TYPE = "HOTEL_DESTINATION_TYPE"
        const val DESTINATION_NAME = "HOTEL_DESTINATION_NAME"
        const val DESTINATION_LAT = "HOTEL_DESTINATION_LAT"
        const val DESTINATION_LONG = "HOTEL_DESTINATION_LONG"
        const val CHECK_IN_DATE = "CHECK_IN_DATE"
        const val CHECK_OUT_DATE = "CHECK_OUT_DATE"
        const val NUM_OF_GUESTS = "NUM_OF_GUESTS"
        const val NUM_OF_ROOMS = "NUM_OF_ROOMS"
        const val SEARCH_TYPE = "SEARCH_TYPE"
        const val SEARCH_ID = "SEARCH_ID"

        fun getIntent(context: Context, destinationId: Long, destinationName: String,
                      destinationType: String, latitude: Double, longitude: Double, 
                      checkInDate: String, checkOutDate: String,
                      numOfGuests: Int, numOfRooms: Int, 
                      searchId: String, searchType: String, title: String = ""): Intent =
                Intent(context, HotelChangeSearchActivity::class.java)
                        .putExtra(EXTRA_DESTINATION_ID, destinationId)
                        .putExtra(EXTRA_DESTINATION_NAME, destinationName)
                        .putExtra(EXTRA_DESTINATION_TYPE, destinationType)
                        .putExtra(EXTRA_DESTINATION_LAT, latitude)
                        .putExtra(EXTRA_DESTINATION_LONG, longitude)
                        .putExtra(EXTRA_CHECK_IN_DATE, checkInDate)
                        .putExtra(EXTRA_CHECK_OUT_DATE, checkOutDate)
                        .putExtra(EXTRA_NUM_OF_GUESTS, numOfGuests)
                        .putExtra(EXTRA_NUM_OF_ROOMS, numOfRooms)
                        .putExtra(EXTRA_DESTINATION_SEARCH_ID, searchId)
                        .putExtra(EXTRA_DESTINATION_SEARCH_TYPE, searchType)
                        .putExtra(EXTRA_TITLE, title)
    }
}