package com.tokopedia.travel_slice.ui.provider

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.slice.Slice
import androidx.slice.SliceProvider
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.travel_slice.hotel.data.HotelData
import com.tokopedia.travel_slice.hotel.data.HotelOrderListModel
import com.tokopedia.travel_slice.di.DaggerTravelSliceComponent
import com.tokopedia.travel_slice.flight.data.FlightOrderListEntity
import com.tokopedia.travel_slice.flight.data.FlightSliceRepository
import com.tokopedia.travel_slice.flight.ui.FlightSliceProviderUtil
import com.tokopedia.travel_slice.hotel.data.HotelSliceRepository
import com.tokopedia.travel_slice.hotel.ui.HotelSliceProviderUtil
import com.tokopedia.travel_slice.hotel.util.TravelDateUtils.validateCheckInDate
import com.tokopedia.travel_slice.utils.TravelSliceStatus
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * @author by jessica on 14/10/20
 */

class MainSliceProvider : SliceProvider() {

    private lateinit var contextNonNull: Context

    @Inject
    lateinit var graphqlRepository: GraphqlRepository

    @Inject
    lateinit var flightSliceRepository: FlightSliceRepository

    @Inject
    lateinit var hotelSliceRepository: HotelSliceRepository

    private var flightOrderList = listOf<FlightOrderListEntity>()
    private var hotelList = listOf<HotelData>()
    private var orderList = listOf<HotelOrderListModel>()
    private var status: TravelSliceStatus = TravelSliceStatus.INIT
    private var checkIn: String = ""
    private var city: String = ""

    override fun onCreateSliceProvider(): Boolean {
        status = TravelSliceStatus.INIT
        LocalCacheHandler(context, APPLINK_DEBUGGER)
        return true
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        contextNonNull = context ?: return null
        init()

        val type = sliceUri.getQueryParameter(ARG_TYPE) ?: TYPE_HOTEL

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return null
        return when (sliceUri.lastPathSegment) {
            BOOK_HOTEL -> createGetBookHotelSlice(sliceUri)
            MY_BOOKING -> if (type == TYPE_FLIGHT) createGetFlightOrderSlice(sliceUri) else createGetHotelOrderSlice(sliceUri)
            else -> null
        }
    }

    private fun init() {
        GraphqlClient.init(contextNonNull)
        DaggerTravelSliceComponent.builder().build().inject(this)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createGetBookHotelSlice(sliceUri: Uri): Slice? {
        if (status == TravelSliceStatus.INIT) {
            status = TravelSliceStatus.LOADING

            getHotelData(sliceUri)
        }
        return when (status) {
            TravelSliceStatus.INIT, TravelSliceStatus.LOADING -> HotelSliceProviderUtil.getLoadingStateSlices(contextNonNull, sliceUri)
            TravelSliceStatus.SUCCESS -> {
                if (hotelList.isEmpty()) HotelSliceProviderUtil.getFailedFetchDataSlices(contextNonNull, sliceUri)
                else HotelSliceProviderUtil.getHotelRecommendationSlices(contextNonNull, sliceUri, city, checkIn, hotelList)
            }
            TravelSliceStatus.FAILURE -> HotelSliceProviderUtil.getFailedFetchDataSlices(contextNonNull, sliceUri)
            else -> HotelSliceProviderUtil.getFailedFetchDataSlices(contextNonNull, sliceUri)
        }
    }

    private fun getHotelData(sliceUri: Uri) {
        CoroutineScope(Dispatchers.IO).launchCatchError(block = {
            //get params from Uri
            city = sliceUri.getQueryParameter(ARG_CITY) ?: DEFAULT_CITY_VALUE
            val checkInOutDate = validateCheckInDate(sliceUri.getQueryParameter(ARG_CHECKIN) ?: "")
            checkIn = checkInOutDate.first

            //get hotel data from API
            hotelList = hotelSliceRepository.getHotelData(city, checkIn, checkInOutDate.second)
            status = TravelSliceStatus.SUCCESS

            //notify data changed
            contextNonNull.contentResolver.notifyChange(sliceUri, null)
        }) {
            status = TravelSliceStatus.FAILURE
            contextNonNull.contentResolver.notifyChange(sliceUri, null)
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createGetHotelOrderSlice(sliceUri: Uri): Slice? {
        if (status == TravelSliceStatus.INIT) {
            status = TravelSliceStatus.LOADING

            getHotelOrderData(sliceUri)
        }
        return when (status) {
            TravelSliceStatus.INIT, TravelSliceStatus.LOADING -> HotelSliceProviderUtil.getLoadingStateSlices(contextNonNull, sliceUri)
            TravelSliceStatus.SUCCESS -> {
                if (orderList.isNotEmpty()) HotelSliceProviderUtil.getMyHotelOrderSlices(contextNonNull, sliceUri, orderList)
                else HotelSliceProviderUtil.getFailedFetchDataSlices(contextNonNull, sliceUri)
            }
            TravelSliceStatus.FAILURE -> HotelSliceProviderUtil.getFailedFetchDataSlices(contextNonNull, sliceUri)
            TravelSliceStatus.USER_NOT_LOG_IN -> HotelSliceProviderUtil.getUserNotLoggedIn(contextNonNull, sliceUri)
        }
    }

    private fun getHotelOrderData(sliceUri: Uri) {
        CoroutineScope(Dispatchers.IO).launchCatchError(block = {
            val isLoggedIn = UserSession(contextNonNull).isLoggedIn
            orderList = hotelSliceRepository.getHotelOrderData(isLoggedIn)

            status = TravelSliceStatus.SUCCESS
            contextNonNull.contentResolver.notifyChange(sliceUri, null)
        }) {
            if (it.message == "unauthorized user") {
                status = TravelSliceStatus.USER_NOT_LOG_IN
                contextNonNull.contentResolver.notifyChange(sliceUri, null)
            } else {
                status = TravelSliceStatus.FAILURE
                contextNonNull.contentResolver.notifyChange(sliceUri, null)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createGetFlightOrderSlice(sliceUri: Uri): Slice? {
        if (status == TravelSliceStatus.INIT) {
            status = TravelSliceStatus.LOADING

            getFlightOrderData(sliceUri)
        }
        return when (status) {
            TravelSliceStatus.INIT, TravelSliceStatus.LOADING -> FlightSliceProviderUtil.getLoadingStateSlices(contextNonNull, sliceUri)
            TravelSliceStatus.SUCCESS -> {
                if (flightOrderList.isNotEmpty()) FlightSliceProviderUtil.getFlightOrderSlices(contextNonNull, sliceUri, flightOrderList)
                else FlightSliceProviderUtil.getFailedFetchDataSlices(contextNonNull, sliceUri)
            }
            TravelSliceStatus.FAILURE -> FlightSliceProviderUtil.getFailedFetchDataSlices(contextNonNull, sliceUri)
            TravelSliceStatus.USER_NOT_LOG_IN -> FlightSliceProviderUtil.getUserNotLoggedIn(contextNonNull, sliceUri)
        }
    }

    private fun getFlightOrderData(sliceUri: Uri) {
        CoroutineScope(Dispatchers.IO).launchCatchError(block = {
            val isLoggedIn = UserSession(contextNonNull).isLoggedIn
            flightOrderList = flightSliceRepository.getFlightOrderData(isLoggedIn)

            status = TravelSliceStatus.SUCCESS
            contextNonNull.contentResolver.notifyChange(sliceUri, null)
        }) {
            if (it.message == "unauthorized user") {
                status = TravelSliceStatus.USER_NOT_LOG_IN
                contextNonNull.contentResolver.notifyChange(sliceUri, null)
            } else {
                status = TravelSliceStatus.FAILURE
                contextNonNull.contentResolver.notifyChange(sliceUri, null)
            }
        }
    }

    companion object {
        const val ARG_TYPE = "type"
        const val ARG_CITY = "city"
        const val ARG_CHECKIN = "checkIn"
        const val DEFAULT_CITY_VALUE = "Jakarta"
        const val DATA_PARAM = "data"
        private val APPLINK_DEBUGGER = "APPLINK_DEBUGGER"

        private const val TYPE_FLIGHT = "Flight"
        private const val TYPE_HOTEL = "Hotel"

        const val BOOK_HOTEL = "book_hotel"
        const val MY_BOOKING = "my_booking"
    }
}