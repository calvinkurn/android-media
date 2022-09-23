package com.tokopedia.hotel.homepage.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.common.util.HotelUtils
import com.tokopedia.hotel.homepage.di.DaggerHotelHomepageComponent
import com.tokopedia.hotel.homepage.di.HotelHomepageComponent
import com.tokopedia.hotel.homepage.presentation.fragment.HotelHomepageFragment
import com.tokopedia.kotlin.extensions.view.toIntSafely

class HotelHomepageActivity : HotelBaseActivity(), HasComponent<HotelHomepageComponent> {

    private var name: String = ""
    private var checkIn: String = ""
    private var checkOut: String = ""
    private var room: Int = 1
    private var adult: Int = 1
    private var searchId: String = ""
    private var searchType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        try{
            if (uri != null) {
                // for applink
                if (!uri.getQueryParameter(PARAM_ID).isNullOrEmpty()) {
                    searchId = uri.getQueryParameter(PARAM_ID) ?: ""
                    searchType = uri.getQueryParameter(PARAM_TYPE) ?: ""
                    name = uri.getQueryParameter(PARAM_NAME) ?: ""
                }

                checkIn = uri.getQueryParameter(PARAM_CHECK_IN) ?: ""
                checkOut = uri.getQueryParameter(PARAM_CHECK_OUT) ?: ""
                room = uri.getQueryParameter(PARAM_ROOM)?.toIntSafely() ?: 0
                adult = uri.getQueryParameter(PARAM_ADULT)?.toIntSafely() ?: 0

                name = name.replace(SPACE_ENCODED, " ")
            }
        }catch (t: Throwable){
            //to prevent unsuccessful parsing, use default value
        }
        checkParameter()

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
            //other: when homepage accessed without param (default)
            else -> HotelHomepageFragment.getInstance()
        }

    }

    override fun shouldShowOptionMenu(): Boolean = true

    private fun checkParameter() {
        val updatedCheckInCheckOutDate = HotelUtils.validateCheckInAndCheckOutDate(checkIn, checkOut)
        checkIn = updatedCheckInCheckOutDate.first
        checkOut = updatedCheckInCheckOutDate.second
    }

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

        const val HOMEPAGE_SCREEN_NAME = "/hotel/homepage"

        private const val SPACE_ENCODED = "%20"
    }
}
