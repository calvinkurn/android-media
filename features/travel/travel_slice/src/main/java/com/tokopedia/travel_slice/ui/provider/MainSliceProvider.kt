package com.tokopedia.travel_slice.ui.provider

import android.app.PendingIntent
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.SliceProvider
import androidx.slice.builders.*
import com.tokopedia.applink.RouteManager
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.travel_slice.R
import com.tokopedia.travel_slice.data.HotelList
import com.tokopedia.travel_slice.di.DaggerTravelSliceComponent
import com.tokopedia.travel_slice.usecase.GetPropertiesUseCase
import com.tokopedia.travel_slice.usecase.GetSuggestionCityUseCase
import com.tokopedia.travel_slice.utils.TravelDateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * @author by jessica on 14/10/20
 */

class MainSliceProvider : SliceProvider() {

    private lateinit var contextNonNull: Context

    @Inject
    lateinit var graphqlRepository: GraphqlRepository

    private val getSuggestionCityUseCase by lazy { GetSuggestionCityUseCase(graphqlRepository) }
    private val getPropertiesUseCase: GetPropertiesUseCase by lazy { GetPropertiesUseCase(graphqlRepository) }

    private var hotelList = HotelList()
    private var status: Status = Status.INIT

    override fun onCreateSliceProvider(): Boolean {
        contextNonNull = context?.applicationContext ?: return false
        status = Status.INIT
        return true
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
             createGetHotelSlice(sliceUri)
        } else null
    }

    private fun createGetHotelSlice(sliceUri: Uri): Slice? {
        GraphqlClient.init(contextNonNull)
        DaggerTravelSliceComponent.builder().build().inject(this)
        val mainPendingIntent = PendingIntent.getActivity(
                contextNonNull,
                0,
                RouteManager.getIntent(contextNonNull, "tokopedia://hotel/dashboard"),
                0
        )

        if (status == Status.INIT) {
            status = Status.LOADING
            getHotelData(sliceUri)
        }

        return list(contextNonNull, sliceUri, ListBuilder.INFINITY) {
            header {
                title = "Hotel"
                when (status) {
                    Status.LOADING, Status.INIT -> subtitle = "Loading ..."
                    Status.FAILURE -> subtitle = "Gagal mendapatkan hotel"
                }
                primaryAction = SliceAction.create(
                        mainPendingIntent,
                        IconCompat.createWithResource(contextNonNull, R.drawable.tab_indicator_ab_tokopedia),
                        ListBuilder.ICON_IMAGE,
                        ""
                )
            }
            if (status == Status.SUCCESS) {
                hotelList.propertyList.forEach {
                    row {
                        title = it.name
                        subtitle = it.roomPrice.firstOrNull()?.totalPrice ?: ""
                    }
                }
            }
        }
    }

    private fun getHotelData(sliceUri: Uri): HotelList {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                getSuggestionCityUseCase.cityParam = sliceUri.getQueryParameter(ARG_CITY)
                        ?: DEFAULT_CITY_VALUE
                val cityIdResponse = getSuggestionCityUseCase.executeOnBackground().firstOrNull()
                if (cityIdResponse != null) {
                    val checkInOutDate = validateCheckInDate(sliceUri.getQueryParameter(ARG_CHECKIN) ?: "")
                    getPropertiesUseCase.createParam(checkInOutDate.first, checkInOutDate.second, cityIdResponse)
                    hotelList = getPropertiesUseCase.executeOnBackground()
                    status = Status.SUCCESS
                    contextNonNull.contentResolver.notifyChange(sliceUri, null)
                }
            } catch (e: Exception) {
                Log.d("ERRORR", e.localizedMessage)
                status = Status.FAILURE
                contextNonNull.contentResolver.notifyChange(sliceUri, null)
            }
        }
        return hotelList
    }

    private fun validateCheckInDate(checkIn: String): Pair<String, String> {
        var checkInTemp = checkIn
        if (checkIn.isEmpty()) checkInTemp = TravelDateUtils.getTodayDate(TravelDateUtils.YYYY_MM_DD_T_HH_MM_SS)
        val checkInString = TravelDateUtils.formatDate(TravelDateUtils.YYYY_MM_DD_T_HH_MM_SS, TravelDateUtils.YYYY_MM_DD, checkInTemp)
        val checkOut = TravelDateUtils.addTimeToSpesificDate(TravelDateUtils.stringToDate(TravelDateUtils.YYYY_MM_DD, checkInString), Calendar.DATE, 1)
        val checkOutString = TravelDateUtils.dateToString(TravelDateUtils.YYYY_MM_DD, checkOut)
        return Pair(checkInString, checkOutString)
    }

    enum class Status { SUCCESS, FAILURE, LOADING, INIT }

    companion object {
        const val ARG_CITY = "city"
        const val ARG_CHECKIN = "checkIn"
        const val DEFAULT_CITY_VALUE = "Jakarta"
        const val DATA_PARAM = "data"
    }
}