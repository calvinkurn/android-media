package com.tokopedia.hotel.globalsearch.presentation.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.databinding.FragmentHotelChangeSearchBinding
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
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.addTimeToSpesificDate
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.date.toString
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*

/**
 * @author by jessica on 06/04/20
 */

class HotelChangeSearchFragment : HotelGlobalSearchFragment() {

    private val trackingUtil: TrackingHotelUtil by lazy { TrackingHotelUtil() }
    private var binding by autoClearedNullable<FragmentHotelChangeSearchBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHotelChangeSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { ctx ->
            binding?.tvHotelHomepageDestination?.typeface =
                Typography.getFontType(ctx, false, Typography.DISPLAY_1)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            globalSearchModel.destinationId = it.getLong(EXTRA_DESTINATION_ID, 0)
            globalSearchModel.destinationName = it.getString(EXTRA_DESTINATION_NAME) ?: ""
            globalSearchModel.destinationType = it.getString(EXTRA_DESTINATION_TYPE) ?: ""
            globalSearchModel.destinationType
            globalSearchModel.checkInDate = it.getString(EXTRA_CHECK_IN_DATE)
                ?: DateUtil.getCurrentDate().addTimeToSpesificDate(Calendar.DATE, 1)
                    .toString(DateUtil.YYYY_MM_DD)
            globalSearchModel.checkInDateFmt =
                it.getString(EXTRA_CHECK_IN_DATE).toDate(DateUtil.YYYY_MM_DD)
                    .toString(DateUtil.DEFAULT_VIEW_FORMAT)
            globalSearchModel.checkOutDate = it.getString(EXTRA_CHECK_OUT_DATE)
                ?: DateUtil.getCurrentDate().addTimeToSpesificDate(Calendar.DATE, 2)
                    .toString(DateUtil.YYYY_MM_DD)
            globalSearchModel.checkOutDateFmt =
                it.getString(EXTRA_CHECK_OUT_DATE).toDate(DateUtil.YYYY_MM_DD)
                    .toString(DateUtil.DEFAULT_VIEW_FORMAT)
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
        val type: String =
            if (globalSearchModel.searchType.isNotEmpty()) globalSearchModel.searchType
            else globalSearchModel.destinationType

        trackingUtil.clickSaveChangeSearch(
            context,
            type,
            globalSearchModel.destinationName,
            globalSearchModel.numOfRooms,
            globalSearchModel.numOfGuests,
            globalSearchModel.checkInDate,
            globalSearchModel.checkOutDate,
            SCREEN_NAME
        )

        when {
            globalSearchModel.destinationType.equals(HotelTypeEnum.PROPERTY.value, false) -> {
                context?.let {
                    startActivityForResult(
                        HotelDetailActivity.getCallingIntent(
                            it,
                            globalSearchModel.checkInDate,
                            globalSearchModel.checkOutDate,
                            globalSearchModel.destinationId,
                            globalSearchModel.numOfRooms,
                            globalSearchModel.numOfGuests,
                            globalSearchModel.destinationType,
                            globalSearchModel.destinationName
                        ),
                        REQUEST_CODE_DETAIL
                    )
                }
            }

            globalSearchModel.searchType.equals(HotelTypeEnum.PROPERTY.value, false) -> {
                context?.let {
                    startActivityForResult(
                        HotelDetailActivity.getCallingIntent(
                            it,
                            globalSearchModel.checkInDate,
                            globalSearchModel.checkOutDate,
                            globalSearchModel.searchId.toLong(),
                            globalSearchModel.numOfRooms,
                            globalSearchModel.numOfGuests,
                            globalSearchModel.searchType,
                            globalSearchModel.destinationName
                        ),
                        REQUEST_CODE_DETAIL
                    )
                }
            }

            else -> {
                val intent = Intent().apply {
                    putExtra(DESTINATION_ID, globalSearchModel.destinationId)
                    putExtra(DESTINATION_TYPE, globalSearchModel.destinationType)
                    putExtra(DESTINATION_NAME, globalSearchModel.destinationName)
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
        binding?.tvHotelHomepageDestination?.let {
            it.setText(globalSearchModel.destinationName)
            it.setOnClickListener { onDestinationChangeClicked() }
        }
        binding?.layoutGlobalSearch?.let {
            it.tvHotelHomepageCheckinDate.setText(data.checkInDateFmt)
            it.tvHotelHomepageCheckoutDate.setText(data.checkOutDateFmt)
            it.tvHotelHomepageNightCount.text = data.nightCount.toString()
            it.tvHotelHomepageGuestInfo.setText(
                String.format(
                    getString(R.string.hotel_homepage_guest_detail_without_child),
                    data.numOfRooms, data.numOfGuests
                )
            )

            it.tvHotelHomepageCheckinDate.setOnClickListener { configAndRenderCheckInDate() }
            it.tvHotelHomepageCheckoutDate.setOnClickListener { configAndRenderCheckOutDate() }
            it.tvHotelHomepageGuestInfo.setOnClickListener { onGuestInfoClicked() }
            it.btnHotelHomepageSearch.setOnClickListener { onCheckAvailabilityClicked() }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_DESTINATION -> if (resultCode == RESULT_OK && data != null) {
                when {
                    data.hasExtra(HotelDestinationActivity.HOTEL_CURRENT_LOCATION_LANG) -> {
                        onDestinationNearBy(
                            data.getDoubleExtra(
                                HotelDestinationActivity.HOTEL_CURRENT_LOCATION_LANG,
                                0.0
                            ),
                            data.getDoubleExtra(
                                HotelDestinationActivity.HOTEL_CURRENT_LOCATION_LAT,
                                0.0
                            )
                        )
                    }
                    data.hasExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_ID) -> {
                        onDestinationChanged(
                            data.getStringExtra(HotelDestinationActivity.HOTEL_DESTINATION_NAME)
                                ?: "",
                            searchId = data.getStringExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_ID)
                                ?: "",
                            searchType = data.getStringExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_TYPE)
                                ?: ""
                        )
                    }
                }
            }
        }
    }

    private fun onDestinationChangeClicked() {
        context?.run {
            startActivityForResult(
                HotelDestinationActivity.createInstance(this),
                REQUEST_CODE_DESTINATION
            )
        }
        activity?.overridePendingTransition(
            com.tokopedia.common.travel.R.anim.travel_slide_up_in,
            com.tokopedia.common.travel.R.anim.travel_anim_stay
        )
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

    private fun onDestinationChanged(
        name: String, destinationId: Long = 0, type: String = "",
        searchId: String = "", searchType: String = ""
    ) {
        globalSearchModel.destinationName = name
        globalSearchModel.destinationId = destinationId
        globalSearchModel.destinationType = type
        globalSearchModel.locationLat = 0.0
        globalSearchModel.locationLong = 0.0
        globalSearchModel.searchType = searchType
        globalSearchModel.searchId = searchId
        renderView()
    }

    companion object {

        const val REQUEST_CODE_DESTINATION = 101
        const val REQUEST_CODE_DETAIL = 103
        const val SCREEN_NAME = "/hotel/changesearch"

        fun getInstance(
            destinationId: Long,
            destinationName: String,
            destinationType: String,
            latitude: Double,
            longitude: Double,
            checkInDate: String,
            checkOutDate: String,
            numOfGuests: Int,
            numOfRooms: Int,
            searchId: String,
            searchType: String
        ) =
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