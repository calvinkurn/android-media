package com.tokopedia.travel_slice.ui.provider

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.slice.Slice
import androidx.slice.SliceProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.travel_slice.analytics.TravelSliceAnalytics
import com.tokopedia.travel_slice.di.DaggerTravelSliceComponent
import com.tokopedia.travel_slice.flight.data.FlightOrderListEntity
import com.tokopedia.travel_slice.flight.data.FlightSliceRepository
import com.tokopedia.travel_slice.flight.ui.FlightSliceProviderUtil
import com.tokopedia.travel_slice.hotel.data.HotelData
import com.tokopedia.travel_slice.hotel.data.HotelOrderListModel
import com.tokopedia.travel_slice.hotel.data.HotelSliceRepository
import com.tokopedia.travel_slice.hotel.ui.HotelSliceProviderUtil
import com.tokopedia.travel_slice.hotel.util.TravelDateUtils.validateCheckInDate
import com.tokopedia.travel_slice.utils.TravelSliceStatus
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
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

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analytics: TravelSliceAnalytics

    private var sliceHashMap: HashMap<Uri, Slice> = HashMap()
    private var isAlreadyInit: Boolean = false

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
        if (!isAlreadyInit) {
            contextNonNull = context ?: return null
            init()
            isAlreadyInit = true
        }

        sliceHashMap[sliceUri]?.let {
            return it
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return null
        return when (sliceUri.lastPathSegment) {
            BOOK_HOTEL -> createGetBookHotelSlice(sliceUri)
            MY_BOOKING -> {
                val type = sliceUri.getQueryParameter(ARG_TYPE) ?: TYPE_HOTEL
                if (type.equals(TYPE_FLIGHT, true)) createGetFlightOrderSlice(sliceUri)
                else createGetHotelOrderSlice(sliceUri)
            }
            else -> null
        }
    }

    private fun init() {
        GraphqlClient.init(contextNonNull)
        DaggerTravelSliceComponent.builder()
                .baseAppComponent((requireNotNull(context).applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createGetBookHotelSlice(sliceUri: Uri): Slice? {
        if (status == TravelSliceStatus.INIT) {
            status = TravelSliceStatus.LOADING

            getHotelData(sliceUri)
        }
        val slice = when (status) {
            TravelSliceStatus.INIT, TravelSliceStatus.LOADING -> HotelSliceProviderUtil.getLoadingStateSlices(contextNonNull, sliceUri)
            TravelSliceStatus.SUCCESS -> {
                analytics.viewHotelBooking(contextNonNull, isSuccess = true, isLogin = userSession.isLoggedIn)
                if (hotelList.isEmpty()) HotelSliceProviderUtil.getEmptyDataSlices(contextNonNull, sliceUri)
                else HotelSliceProviderUtil.getHotelRecommendationSlices(contextNonNull, sliceUri, city, checkIn, hotelList)
            }
            TravelSliceStatus.FAILURE -> {
                analytics.viewHotelBooking(contextNonNull, isSuccess = false, isLogin = userSession.isLoggedIn)
                HotelSliceProviderUtil.getFailedFetchDataSlices(contextNonNull, sliceUri)
            }
            else -> {
                analytics.viewHotelBooking(contextNonNull, isSuccess = false, isLogin = userSession.isLoggedIn)
                status = TravelSliceStatus.FAILURE
                HotelSliceProviderUtil.getFailedFetchDataSlices(contextNonNull, sliceUri)
            }
        }

        if (status == TravelSliceStatus.SUCCESS || status == TravelSliceStatus.FAILURE) {
            status = TravelSliceStatus.INIT
            sliceHashMap[sliceUri] = slice
        }
        return slice
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

        val slice = when (status) {
            TravelSliceStatus.INIT, TravelSliceStatus.LOADING -> HotelSliceProviderUtil.getLoadingStateSlices(contextNonNull, sliceUri)
            TravelSliceStatus.SUCCESS -> {
                analytics.viewHotelReservation(contextNonNull, true, userSession.isLoggedIn)
                if (orderList.isNotEmpty()) HotelSliceProviderUtil.getMyHotelOrderSlices(contextNonNull, sliceUri, orderList)
                else HotelSliceProviderUtil.getEmptyOrderListSlices(contextNonNull, sliceUri)
            }
            TravelSliceStatus.FAILURE -> {
                analytics.viewHotelReservation(contextNonNull, false, userSession.isLoggedIn)
                HotelSliceProviderUtil.getFailedFetchDataSlices(contextNonNull, sliceUri)
            }
            TravelSliceStatus.USER_NOT_LOG_IN -> {
                analytics.viewHotelReservation(contextNonNull, false, userSession.isLoggedIn)
                status = TravelSliceStatus.INIT
                HotelSliceProviderUtil.getUserNotLoggedIn(contextNonNull, sliceUri)
            }
        }

        if (status == TravelSliceStatus.SUCCESS || status == TravelSliceStatus.FAILURE) {
            status = TravelSliceStatus.INIT
            sliceHashMap[sliceUri] = slice
        }
        return slice
    }

    private fun getHotelOrderData(sliceUri: Uri) {
        CoroutineScope(Dispatchers.IO).launchCatchError(block = {
            val isLoggedIn = userSession.isLoggedIn
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
        val slice = when (status) {
            TravelSliceStatus.INIT, TravelSliceStatus.LOADING -> FlightSliceProviderUtil.getLoadingStateSlices(contextNonNull, sliceUri)
            TravelSliceStatus.SUCCESS -> {
                analytics.viewFlightReservation(contextNonNull, userSession.isLoggedIn, true)
                if (flightOrderList.isNotEmpty()) FlightSliceProviderUtil.getFlightOrderSlices(contextNonNull, sliceUri, flightOrderList)
                else FlightSliceProviderUtil.getEmptyOrderListSlices(contextNonNull, sliceUri)
            }
            TravelSliceStatus.FAILURE -> {
                analytics.viewFlightReservation(contextNonNull, userSession.isLoggedIn, false)
                FlightSliceProviderUtil.getFailedFetchDataSlices(contextNonNull, sliceUri)
            }
            TravelSliceStatus.USER_NOT_LOG_IN -> {
                analytics.viewFlightReservation(contextNonNull, userSession.isLoggedIn, false)
                status = TravelSliceStatus.INIT
                FlightSliceProviderUtil.getUserNotLoggedIn(contextNonNull, sliceUri)
            }
        }

        if (status == TravelSliceStatus.SUCCESS || status == TravelSliceStatus.FAILURE) {
            status = TravelSliceStatus.INIT
            sliceHashMap[sliceUri] = slice
        }
        return slice
    }

    private fun getFlightOrderData(sliceUri: Uri) {
        CoroutineScope(Dispatchers.IO).launchCatchError(block = {
            val isLoggedIn = userSession.isLoggedIn
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
        const val ARG_TYPE = "traveltype"
        const val ARG_CITY = "city"
        const val ARG_CHECKIN = "checkIn"
        const val DEFAULT_CITY_VALUE = "Jakarta"
        const val DATA_PARAM = "data"
        private val APPLINK_DEBUGGER = "APPLINK_DEBUGGER"

        private const val TYPE_FLIGHT = "flight"
        private const val TYPE_HOTEL = "hotel"

        const val BOOK_HOTEL = "book_hotel"
        const val MY_BOOKING = "my_booking"
    }
}