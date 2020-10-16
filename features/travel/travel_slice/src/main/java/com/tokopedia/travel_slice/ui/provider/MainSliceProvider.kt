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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.travel_slice.R
import com.tokopedia.travel_slice.data.*
import com.tokopedia.travel_slice.di.DaggerTravelSliceComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by jessica on 14/10/20
 */

class MainSliceProvider : SliceProvider() {

    private lateinit var contextNonNull: Context

    @Inject
    lateinit var repository: GraphqlRepository

    override fun onCreateSliceProvider(): Boolean {
        contextNonNull = context?.applicationContext ?: return false
        return true
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) createGetHotelSlice(sliceUri) else null
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
                if (hotelList.propertyList.isEmpty() && status == Status.LOADING) subtitle = "Loading ..."
                primaryAction = SliceAction.create(
                        mainPendingIntent,
                        IconCompat.createWithResource(contextNonNull, R.drawable.tab_indicator_ab_tokopedia),
                        ListBuilder.ICON_IMAGE,
                        ""
                )
            }
            if (hotelList.propertyList.isNotEmpty()) {
                hotelList.propertyList.forEach {
                    row {
                        title = it.name
                        subtitle = it.roomPrice.firstOrNull()?.totalPrice ?: ""
                    }
                }
            }
        }
    }

    var hotelList = HotelList()
    var status: Status = Status.INIT

    private fun getHotelData(sliceUri: Uri): HotelList {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val destinationString = sliceUri.getQueryParameter("city") ?: ""
                val getSuggestionParams = mapOf("data" to SearchSuggestionParam(destinationString))
                val suggestionGraphqlRequest = GraphqlRequest(TravelSliceQuery.SEARCH_SUGGESTION,
                        SuggestionCity.Response::class.java, getSuggestionParams)
                val cityResponse = repository.getReseponse(listOf(suggestionGraphqlRequest))
                        .getSuccessData<SuggestionCity.Response>()

                if (cityResponse.response.data.isNotEmpty()) {

                    val params = mapOf("data" to HotelParam(checkIn = "2020-10-20", checkOut = "2020-10-22",
                                location = HotelParam.ParamLocation(searchType = cityResponse.response.data.first().searchType,
                                    searchId = cityResponse.response.data.first().searchId )))
                    val graphqlRequest = GraphqlRequest(TravelSliceQuery.GET_HOTEL_PROPERTY_QUERY, HotelList.Response::class.java, params)
                    val data = repository.getReseponse(listOf(graphqlRequest)).getSuccessData<HotelList.Response>()
                    hotelList = data.propertySearch

                    status = Status.SUCCESS
                    contextNonNull.contentResolver.notifyChange(sliceUri, null)
                }
            } catch (e: Exception) {
                status = Status.FAILURE
            }
        }
        return hotelList
    }

    enum class Status {SUCCESS, FAILURE, LOADING, INIT}
}