package com.tokopedia.hotel.search_map.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.common.util.HotelUtils
import com.tokopedia.hotel.search_map.data.model.HotelSearchModel
import com.tokopedia.hotel.search_map.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search_map.di.DaggerHotelSearchMapComponent
import com.tokopedia.hotel.search_map.di.HotelSearchMapComponent
import com.tokopedia.hotel.search_map.presentation.fragment.HotelSearchMapFragment
import com.tokopedia.kotlin.extensions.view.toIntSafely

class HotelSearchMapActivity : HotelBaseActivity(), HasComponent<HotelSearchMapComponent> {

    private var hotelSearchModel = HotelSearchModel()
    private var selectedParam = ParamFilterV2()
    private var selectedSort: String = ""

    override fun getScreenName(): String = SEARCH_SCREEN_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        try{
            if (uri != null) {
                //for newer applink
                if (!uri.getQueryParameter(PARAM_ID).isNullOrEmpty()) {
                    hotelSearchModel.searchId = uri.getQueryParameter(PARAM_ID)
                        ?: ""
                    hotelSearchModel.searchType = uri.getQueryParameter(PARAM_TYPE)
                        ?: ""
                    hotelSearchModel.name = uri.getQueryParameter(PARAM_NAME)
                        ?: ""
                }

                hotelSearchModel.checkIn = uri.getQueryParameter(PARAM_CHECK_IN)
                    ?: ""
                hotelSearchModel.checkOut = uri.getQueryParameter(PARAM_CHECK_OUT)
                    ?: ""
                hotelSearchModel.room = uri.getQueryParameter(PARAM_ROOM)?.toIntSafely()
                    ?: 1
                hotelSearchModel.adult = uri.getQueryParameter(PARAM_ADULT)?.toIntSafely()
                    ?: 1

                selectedParam.name = uri.getQueryParameter(PARAM_FILTER_ID)
                    ?: ""
                val values: String = uri.getQueryParameter(PARAM_FILTER_VALUE)
                    ?: ""
                selectedParam.values = values.split(",").toMutableList()

                selectedSort = uri.getQueryParameter(PARAM_SORT) ?: ""
            } else {
                //when activity open from intent
                hotelSearchModel = intent.getParcelableExtra(HotelSearchMapFragment.ARG_HOTEL_SEARCH_MODEL)
                    ?: HotelSearchModel()
            }
        } catch (t: Throwable){
            //to prevent unsuccessful parsing, use default value
        }

        checkParameter()

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment? =
            HotelSearchMapFragment.createInstance(hotelSearchModel, selectedParam, selectedSort)

    override fun getComponent(): HotelSearchMapComponent =
            DaggerHotelSearchMapComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()

    private fun checkParameter() {
        val updatedCheckInCheckOutDate = HotelUtils.validateCheckInAndCheckOutDate(hotelSearchModel.checkIn, hotelSearchModel.checkOut)
        hotelSearchModel.checkIn = updatedCheckInCheckOutDate.first
        hotelSearchModel.checkOut = updatedCheckInCheckOutDate.second
    }

    companion object {
        const val SEARCH_SCREEN_NAME = "/hotel/searchresult"

        const val PARAM_HOTEL_ID = "hotel_id"
        const val PARAM_HOTEL_NAME = "hotel_name"
        const val PARAM_FILTER_ID = "filter_selected_id"
        const val PARAM_FILTER_VALUE = "filter_selected_value"
        const val PARAM_SORT = "sort"

        const val PARAM_CHECK_IN = "check_in"
        const val PARAM_CHECK_OUT = "check_out"
        const val PARAM_ROOM = "room"
        const val PARAM_ADULT = "adult"

        //newer version applink param
        const val PARAM_ID = "id"
        const val PARAM_NAME = "name"
        const val PARAM_TYPE = "type"

        fun createIntent(context: Context, hotelSearchModel: HotelSearchModel): Intent =
                Intent(context, HotelSearchMapActivity::class.java)
                        .putExtra(HotelSearchMapFragment.ARG_HOTEL_SEARCH_MODEL, hotelSearchModel)
    }

}