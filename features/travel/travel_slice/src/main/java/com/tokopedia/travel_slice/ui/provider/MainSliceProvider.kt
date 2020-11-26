package com.tokopedia.travel_slice.ui.provider

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.slice.Slice
import androidx.slice.SliceProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.travel_slice.data.HotelData
import com.tokopedia.travel_slice.data.HotelOrderListModel
import com.tokopedia.travel_slice.di.DaggerTravelSliceComponent
import com.tokopedia.travel_slice.ui.provider.HotelSliceProviderUtil.allowReads
import com.tokopedia.travel_slice.utils.TravelDateUtils.validateCheckInDate
import com.tokopedia.travel_slice.utils.TravelSliceStatus
import com.tokopedia.usecase.launch_cache_error.launchCatchError
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
    lateinit var hotelSliceRepository: HotelSliceRepository

    private var hotelList = listOf<HotelData>()
    private var orderList = listOf<HotelOrderListModel>()
    private var status: TravelSliceStatus = TravelSliceStatus.INIT
    private var checkIn: String = ""
    private var city: String = ""

    override fun onCreateSliceProvider(): Boolean {
        status = TravelSliceStatus.INIT
        return true
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        contextNonNull = context ?: return null
        init()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return null
        return when (sliceUri.lastPathSegment) {
            BOOK_HOTEL -> createGetBookHotelSlice(sliceUri)
            MY_BOOKING -> createGetHotelOrderSlice(sliceUri)
            else -> null
        }
    }

    private fun init() {
        allowReads {
            GraphqlClient.init(contextNonNull)
        }
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

    companion object {
        const val ARG_CITY = "city"
        const val ARG_CHECKIN = "checkIn"
        const val DEFAULT_CITY_VALUE = "Jakarta"
        const val DATA_PARAM = "data"

        const val BOOK_HOTEL = "book_hotel"
        const val MY_BOOKING = "my_booking"
    }
}