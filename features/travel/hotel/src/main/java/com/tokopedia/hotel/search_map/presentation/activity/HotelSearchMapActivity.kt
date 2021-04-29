package com.tokopedia.hotel.search_map.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.common.util.HotelUtils
import com.tokopedia.hotel.search.data.model.HotelSearchModel
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.presentation.activity.HotelSearchResultActivity
import com.tokopedia.hotel.search.presentation.fragment.HotelSearchResultFragment
import com.tokopedia.hotel.search_map.di.DaggerHotelSearchMapComponent
import com.tokopedia.hotel.search_map.di.HotelSearchMapComponent
import com.tokopedia.hotel.search_map.presentation.fragment.HotelSearchMapFragment

class HotelSearchMapActivity : HotelBaseActivity(), HasComponent<HotelSearchMapComponent> {

    private var hotelSearchModel = HotelSearchModel()
    private var selectedParam = ParamFilterV2()

    override fun getScreenName(): String = HotelSearchResultActivity.SEARCH_SCREEN_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            //for newer applink
            if (!uri.getQueryParameter(HotelSearchResultActivity.PARAM_ID).isNullOrEmpty()) {
                hotelSearchModel.searchId = uri.getQueryParameter(HotelSearchResultActivity.PARAM_ID)
                        ?: ""
                hotelSearchModel.searchType = uri.getQueryParameter(HotelSearchResultActivity.PARAM_TYPE)
                        ?: ""
                hotelSearchModel.name = uri.getQueryParameter(HotelSearchResultActivity.PARAM_NAME)
                        ?: ""
            } else {
                // for older applink
                when {
                    !uri.getQueryParameter(HotelSearchResultActivity.PARAM_HOTEL_ID).isNullOrEmpty() -> {
                        hotelSearchModel.id = (uri.getQueryParameter(HotelSearchResultActivity.PARAM_HOTEL_ID)
                                ?: "0").toLong()
                        hotelSearchModel.name = uri.getQueryParameter(HotelSearchResultActivity.PARAM_HOTEL_NAME)
                                ?: ""
                        hotelSearchModel.type = HotelTypeEnum.PROPERTY.value
                    }
                    !uri.getQueryParameter(HotelSearchResultActivity.PARAM_CITY_ID).isNullOrEmpty() -> {
                        hotelSearchModel.id = (uri.getQueryParameter(HotelSearchResultActivity.PARAM_CITY_ID)
                                ?: "0").toLong()
                        hotelSearchModel.name = uri.getQueryParameter(HotelSearchResultActivity.PARAM_CITY_NAME)
                                ?: ""
                        hotelSearchModel.type = HotelTypeEnum.CITY.value
                    }
                    !uri.getQueryParameter(HotelSearchResultActivity.PARAM_DISTRICT_ID).isNullOrEmpty() -> {
                        hotelSearchModel.id = (uri.getQueryParameter(HotelSearchResultActivity.PARAM_DISTRICT_ID)
                                ?: "0").toLong()
                        hotelSearchModel.name = uri.getQueryParameter(HotelSearchResultActivity.PARAM_DISTRICT_NAME)
                                ?: ""
                        hotelSearchModel.type = HotelTypeEnum.DISTRICT.value
                    }
                    !uri.getQueryParameter(HotelSearchResultActivity.PARAM_REGION_ID).isNullOrEmpty() -> {
                        hotelSearchModel.id = (uri.getQueryParameter(HotelSearchResultActivity.PARAM_REGION_ID)
                                ?: "0").toLong()
                        hotelSearchModel.name = uri.getQueryParameter(HotelSearchResultActivity.PARAM_REGION_NAME)
                                ?: ""
                        hotelSearchModel.type = HotelTypeEnum.REGION.value
                    }
                }
            }

            hotelSearchModel.checkIn = uri.getQueryParameter(HotelSearchResultActivity.PARAM_CHECK_IN)
                    ?: ""
            hotelSearchModel.checkOut = uri.getQueryParameter(HotelSearchResultActivity.PARAM_CHECK_OUT)
                    ?: ""
            hotelSearchModel.room = uri.getQueryParameter(HotelSearchResultActivity.PARAM_ROOM)?.toInt()
                    ?: 1
            hotelSearchModel.adult = uri.getQueryParameter(HotelSearchResultActivity.PARAM_ADULT)?.toInt()
                    ?: 1

            selectedParam.name = uri.getQueryParameter(HotelSearchResultActivity.PARAM_FILTER_ID)
                    ?: ""
            val values: String = uri.getQueryParameter(HotelSearchResultActivity.PARAM_FILTER_VALUE)
                    ?: ""
            selectedParam.values = values.split(",").toMutableList()
        } else {
            //when activity open from intent
            hotelSearchModel = intent.getParcelableExtra(HotelSearchResultFragment.ARG_HOTEL_SEARCH_MODEL)
                    ?: HotelSearchModel()
        }

        checkParameter()

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment? =
            HotelSearchMapFragment.createInstance(hotelSearchModel, selectedParam)

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

        fun createIntent(context: Context, hotelSearchModel: HotelSearchModel): Intent =
                Intent(context, HotelSearchMapActivity::class.java)
                        .putExtra(HotelSearchMapFragment.ARG_HOTEL_SEARCH_MODEL, hotelSearchModel)
    }

}