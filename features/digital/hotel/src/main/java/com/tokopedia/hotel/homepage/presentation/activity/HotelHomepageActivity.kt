package com.tokopedia.hotel.homepage.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.homepage.di.DaggerHotelHomepageComponent
import com.tokopedia.hotel.homepage.di.HotelHomepageComponent
import com.tokopedia.hotel.homepage.presentation.fragment.HotelHomepageFragment

class HotelHomepageActivity : HotelBaseActivity(), HasComponent<HotelHomepageComponent> {

    private var id: Int = 0
    private var name: String = ""
    private var type: String = ""
    private var checkIn: String = ""
    private var checkOut: String = ""
    private var room: Int = 0
    private var adult: Int = 0

    override fun getParentViewResourceID() = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            if (!uri.getQueryParameter(PARAM_HOTEL_ID).isNullOrEmpty()) {
                id = uri.getQueryParameter(PARAM_HOTEL_ID).toInt()
                name = uri.getQueryParameter(PARAM_HOTEL_NAME)
                type = TYPE_PROPERTY
            } else if (!uri.getQueryParameter(PARAM_CITY_ID).isNullOrEmpty()) {
                id = uri.getQueryParameter(PARAM_CITY_ID).toInt()
                name = uri.getQueryParameter(PARAM_CITY_NAME)
                type = TYPE_CITY
            } else if (!uri.getQueryParameter(PARAM_DISTRICT_ID).isNullOrEmpty()) {
                id = uri.getQueryParameter(PARAM_DISTRICT_ID).toInt()
                name = uri.getQueryParameter(PARAM_DISTRICT_NAME)
                type = TYPE_DISTRICT
            } else if (!uri.getQueryParameter(PARAM_REGION_ID).isNullOrEmpty()) {
                id = uri.getQueryParameter(PARAM_REGION_ID).toInt()
                name = uri.getQueryParameter(PARAM_REGION_NAME)
                type = TYPE_REGION
            }

            if (!uri.getQueryParameter(PARAM_CHECK_IN).isNullOrEmpty()) checkIn = uri.getQueryParameter(PARAM_CHECK_IN)
            if (!uri.getQueryParameter(PARAM_CHECK_OUT).isNullOrEmpty()) checkOut = uri.getQueryParameter(PARAM_CHECK_OUT)
            if (!uri.getQueryParameter(PARAM_ROOM).isNullOrEmpty()) room = uri.getQueryParameter(PARAM_ROOM).toInt()
            if (!uri.getQueryParameter(PARAM_ADULT).isNullOrEmpty()) adult = uri.getQueryParameter(PARAM_ADULT).toInt()

        }

        super.onCreate(savedInstanceState)
        toolbar.contentInsetStartWithNavigation = 0
    }

    override fun getComponent(): HotelHomepageComponent =
            DaggerHotelHomepageComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()

    override fun getScreenName(): String = HOMEPAGE_SCREEN_NAME

    override fun getNewFragment(): Fragment = if (type.isNotEmpty())
        HotelHomepageFragment.getInstance(id, name, type, checkIn, checkOut, adult, room)
    else
        HotelHomepageFragment.getInstance()

    override fun shouldShowOptionMenu(): Boolean = true

    companion object {
        fun getCallingIntent(context: Context): Intent =
                Intent(context, HotelHomepageActivity::class.java)

        const val PARAM_HOTEL_ID = "hotel_id"
        const val PARAM_HOTEL_NAME = "hotel_name"
        const val PARAM_DISTRICT_ID = "district_id"
        const val PARAM_DISTRICT_NAME = "district_name"
        const val PARAM_CITY_ID = "city_id"
        const val PARAM_CITY_NAME = "city_name"
        const val PARAM_REGION_ID = "region_id"
        const val PARAM_REGION_NAME = "region_name"
        const val PARAM_CHECK_IN = "check_in"
        const val PARAM_CHECK_OUT = "check_out"
        const val PARAM_ROOM = "room"
        const val PARAM_ADULT = "adult"

        const val TYPE_REGION = "region"
        const val TYPE_DISTRICT = "district"
        const val TYPE_CITY = "city"
        const val TYPE_PROPERTY = "property"

        const val HOMEPAGE_SCREEN_NAME = "/hotel/homepage"
    }
}
