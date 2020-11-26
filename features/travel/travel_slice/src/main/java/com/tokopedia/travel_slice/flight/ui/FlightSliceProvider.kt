package com.tokopedia.travel_slice.flight.ui

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.slice.Slice
import androidx.slice.SliceProvider
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.travel_slice.di.DaggerTravelSliceComponent
import com.tokopedia.travel_slice.flight.data.FlightOrderListEntity
import com.tokopedia.travel_slice.flight.data.FlightSliceRepository
import com.tokopedia.travel_slice.ui.provider.HotelSliceProviderUtil
import com.tokopedia.travel_slice.utils.TravelSliceStatus
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * @author by furqan on 26/11/2020
 */
class FlightSliceProvider : SliceProvider() {

    private lateinit var contextNonNull: Context

    @Inject
    lateinit var flightSliceRepository: FlightSliceRepository

    private var status: TravelSliceStatus = TravelSliceStatus.INIT

    private val orderList: MutableList<FlightOrderListEntity> = arrayListOf()

    override fun onCreateSliceProvider(): Boolean {
        status = TravelSliceStatus.INIT
        return true
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        contextNonNull = context ?: return null
        init()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return null

        return when (sliceUri.lastPathSegment) {
            FLIGHT_BOOKING -> createGetFlightOrderSlice(sliceUri)
            else -> null
        }
    }

    private fun init() {
        HotelSliceProviderUtil.allowReads {
            GraphqlClient.init(contextNonNull)
        }
        DaggerTravelSliceComponent.builder().build().inject(this)
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
                if (orderList.isNotEmpty()) FlightSliceProviderUtil.getFlightOrderSlices(contextNonNull, sliceUri, orderList)
                else FlightSliceProviderUtil.getFailedFetchDataSlices(contextNonNull, sliceUri)
            }
            TravelSliceStatus.FAILURE -> FlightSliceProviderUtil.getFailedFetchDataSlices(contextNonNull, sliceUri)
            TravelSliceStatus.USER_NOT_LOG_IN -> FlightSliceProviderUtil.getUserNotLoggedIn(contextNonNull, sliceUri)
        }
    }

    private fun getFlightOrderData(sliceUri: Uri) {
        CoroutineScope(Dispatchers.IO).launchCatchError(block = {
            val isLoggedIn = UserSession(contextNonNull).isLoggedIn
            orderList.clear()
            orderList.addAll(flightSliceRepository.getFlightOrderData(isLoggedIn))

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
        private const val FLIGHT_BOOKING = "my_booking"
    }
}