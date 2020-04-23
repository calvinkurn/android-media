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

    private var name: String = ""
    private var checkIn: String = ""
    private var checkOut: String = ""
    private var room: Int = 0
    private var adult: Int = 0
    private var searchId: String = ""
    private var searchType: String = ""

    override fun getParentViewResourceID() = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            // for applink
            searchId = uri.getQueryParameter(PARAM_ID) ?: ""
            searchType = uri.getQueryParameter(PARAM_TYPE) ?: ""
            checkIn = uri.getQueryParameter(PARAM_CHECK_IN) ?: ""
            checkOut = uri.getQueryParameter(PARAM_CHECK_OUT) ?: ""
            room = uri.getQueryParameter(PARAM_ROOM)?.toInt() ?: 0
            adult = uri.getQueryParameter(PARAM_ADULT)?.toInt() ?: 0
            name = uri.getQueryParameter(PARAM_NAME) ?: ""
        }

        super.onCreate(savedInstanceState)
        toolbar.contentInsetStartWithNavigation = 0
    }

    override fun getComponent(): HotelHomepageComponent =
            DaggerHotelHomepageComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()

    override fun getScreenName(): String = HOMEPAGE_SCREEN_NAME

    override fun getNewFragment(): Fragment = if (searchId.isNotEmpty())
        HotelHomepageFragment.getInstance(searchId, name, searchType, checkIn, checkOut, adult, room)
    else
        HotelHomepageFragment.getInstance()

    override fun shouldShowOptionMenu(): Boolean = true

    companion object {
        fun getCallingIntent(context: Context): Intent =
                Intent(context, HotelHomepageActivity::class.java)

        const val PARAM_CHECK_IN = "check_in"
        const val PARAM_CHECK_OUT = "check_out"
        const val PARAM_ROOM = "room"
        const val PARAM_ADULT = "adult"
        const val PARAM_ID = "id"
        const val PARAM_TYPE = "type"
        const val PARAM_NAME= "name"

        const val TYPE_PROPERTY = "property"
        const val TYPE_COORDINATE  = "coordinate"

        const val HOMEPAGE_SCREEN_NAME = "/hotel/homepage"
    }
}
