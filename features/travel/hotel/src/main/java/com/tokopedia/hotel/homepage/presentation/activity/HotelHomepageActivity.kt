package com.tokopedia.hotel.homepage.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.homepage.di.DaggerHotelHomepageComponent
import com.tokopedia.hotel.homepage.di.HotelHomepageComponent
import com.tokopedia.hotel.homepage.presentation.fragment.HotelHomepageFragment

class HotelHomepageActivity : HotelBaseActivity(), HasComponent<HotelHomepageComponent> {

    private var name: String = ""
    private var checkIn: String = ""
    private var checkOut: String = ""
    private var room: Int = 0
    private var adult: Int = 0
    private var searchId: String = ""
    private var searchType: String = ""

    //for older applink
    private var id: Long = 0
    private var type: String = ""

    override fun getParentViewResourceID() = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            // for applink
            if (!uri.getQueryParameter(PARAM_ID).isNullOrEmpty()) {
                searchId = uri.getQueryParameter(PARAM_ID) ?: ""
                searchType = uri.getQueryParameter(PARAM_TYPE) ?: ""
                name = uri.getQueryParameter(PARAM_NAME) ?: ""
            } else {
                // for older applink
                when {
                    !uri.getQueryParameter(PARAM_HOTEL_ID).isNullOrEmpty() -> {
                        id = (uri.getQueryParameter(PARAM_HOTEL_ID) ?: "0").toLong()
                        name = uri.getQueryParameter(PARAM_HOTEL_NAME) ?: ""
                        type = HotelTypeEnum.PROPERTY.value
                    }
                    !uri.getQueryParameter(PARAM_CITY_ID).isNullOrEmpty() -> {
                        id = (uri.getQueryParameter(PARAM_CITY_ID) ?: "0").toLong()
                        name = uri.getQueryParameter(PARAM_CITY_NAME) ?: ""
                        type = HotelTypeEnum.CITY.value
                    }
                    !uri.getQueryParameter(PARAM_DISTRICT_ID).isNullOrEmpty() -> {
                        id = (uri.getQueryParameter(PARAM_DISTRICT_ID) ?: "0").toLong()
                        name = uri.getQueryParameter(PARAM_DISTRICT_NAME) ?: ""
                        type = HotelTypeEnum.DISTRICT.value
                    }
                    !uri.getQueryParameter(PARAM_REGION_ID).isNullOrEmpty() -> {
                        id = (uri.getQueryParameter(PARAM_REGION_ID) ?: "0").toLong()
                        name = uri.getQueryParameter(PARAM_REGION_NAME) ?: ""
                        type = HotelTypeEnum.REGION.value
                    }
                }
            }

            checkIn = uri.getQueryParameter(PARAM_CHECK_IN) ?: ""
            checkOut = uri.getQueryParameter(PARAM_CHECK_OUT) ?: ""
            room = uri.getQueryParameter(PARAM_ROOM)?.toInt() ?: 0
            adult = uri.getQueryParameter(PARAM_ADULT)?.toInt() ?: 0

            name = name.replace(SPACE_ENCODED, " ")
        }

        super.onCreate(savedInstanceState)
        toolbar.contentInsetStartWithNavigation = 0
    }

    override fun getComponent(): HotelHomepageComponent =
            DaggerHotelHomepageComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()

    override fun getScreenName(): String = HOMEPAGE_SCREEN_NAME

    override fun getNewFragment(): Fragment {
        return when {
            //case 1: when homepage accessed using searchId && searchType
            searchId.isNotEmpty() -> HotelHomepageFragment.getInstance(searchId, name, searchType, checkIn, checkOut, adult, room)
            //case  2: when user accessed homepage using older version applink
            id != 0L -> HotelHomepageFragment.getInstance(id, name, type, checkIn, checkOut, adult, room)
            //other: when homepage accessed without param (default)
            else -> HotelHomepageFragment.getInstance()
        }

    }

    override fun shouldShowOptionMenu(): Boolean = true

    companion object {
        fun getCallingIntent(context: Context): Intent =
                Intent(context, HotelHomepageActivity::class.java)

        const val PARAM_CHECK_IN = "check_in"
        const val PARAM_CHECK_OUT = "check_out"
        const val PARAM_ROOM = "room"
        const val PARAM_ADULT = "adult"
        const val PARAM_NAME = "name"

        //for searchId and searchType
        const val PARAM_ID = "id"
        const val PARAM_TYPE = "type"

        //for other/older applink
        const val PARAM_HOTEL_ID = "hotel_id"
        const val PARAM_HOTEL_NAME = "hotel_name"
        const val PARAM_DISTRICT_ID = "district_id"
        const val PARAM_DISTRICT_NAME = "district_name"
        const val PARAM_CITY_ID = "city_id"
        const val PARAM_CITY_NAME = "city_name"
        const val PARAM_REGION_ID = "region_id"
        const val PARAM_REGION_NAME = "region_name"

        const val HOMEPAGE_SCREEN_NAME = "/hotel/homepage"

        private const val SPACE_ENCODED = "%20"
    }
}
