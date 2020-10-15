package com.tokopedia.travel_slice.ui.provider

import android.app.PendingIntent
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.SliceProvider
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.SliceAction
import androidx.slice.builders.header
import androidx.slice.builders.list
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.travel_slice.R
import com.tokopedia.travel_slice.data.HotelData
import com.tokopedia.travel_slice.data.HotelList
import com.tokopedia.travel_slice.data.HotelParam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author by jessica on 14/10/20
 */

class MainSliceProvider : SliceProvider() {

    private lateinit var contextNonNull: Context

    var repository: GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    override fun onCreateSliceProvider(): Boolean {
        contextNonNull = context?.applicationContext ?: return false
        return true
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            createGetHotelSlice(sliceUri)
        } else {
            null
        }
    }

    private fun createGetHotelSlice(sliceUri: Uri): Slice? {
        val mainPendingIntent = PendingIntent.getActivity(
                contextNonNull,
                0,
                RouteManager.getIntent(contextNonNull, "tokopedia://hotel/dashboard"),
                0
        )
        val hotelList = getHotelData(sliceUri)
        return list(contextNonNull, sliceUri, ListBuilder.INFINITY) {
            header {
                title = "Hotel"
                if (hotelList.propertyList.isEmpty())
                    subtitle = "Loading ..."
                else
                    subtitle = "Lihat " + hotelList.propertyList.first().name
                primaryAction = SliceAction.create(
                        mainPendingIntent,
                        IconCompat.createWithResource(contextNonNull, R.drawable.tab_indicator_ab_tokopedia),
                        ListBuilder.ICON_IMAGE,
                        ""
                )
            }
        }
    }

    private fun getHotelData(sliceUri: Uri): HotelList {
        val params = mapOf("data" to HotelParam(checkIn = "2020-10-20", checkOut = "2020-10-22"))
        val graphqlRequest = GraphqlRequest(TravelSliceQuery.GET_HOTEL_PROPERTY_QUERY, HotelData::class.java, params)
        GraphqlClient.init(contextNonNull)
        var hotelList = HotelList()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val data = repository.getReseponse(listOf(graphqlRequest)).getSuccessData<HotelList.Response>()
                hotelList = data.propertySearch
                contextNonNull.contentResolver.notifyChange(sliceUri, null)
            } catch (e: Exception) {

            }
        }
        return hotelList
    }

}