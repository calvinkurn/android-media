package com.tokopedia.hotel.homepage.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
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
        }

        super.onCreate(savedInstanceState)
        toolbar.contentInsetStartWithNavigation = 0
    }

    override fun getComponent(): HotelHomepageComponent =
            DaggerHotelHomepageComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()

    override fun getScreenName(): String = ""

    override fun getNewFragment(): Fragment = if (type.isNotEmpty())
        HotelHomepageFragment.getInstance(id, name, type)
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

        const val TYPE_REGION = "region"
        const val TYPE_DISTRICT = "district"
        const val TYPE_CITY = "city"
        const val TYPE_PROPERTY = "property"
    }
}
