package com.tokopedia.hotel.homepage.presentation.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.common.applink.ApplinkConstant
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.homepage.di.DaggerHotelHomepageComponent
import com.tokopedia.hotel.homepage.di.HotelHomepageComponent
import com.tokopedia.hotel.homepage.presentation.fragment.HotelHomepageFragment

class HotelHomepageActivity : HotelBaseActivity(), HasComponent<HotelHomepageComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.contentInsetStartWithNavigation = 0
    }

    override fun getComponent(): HotelHomepageComponent =
            DaggerHotelHomepageComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()

    override fun getScreenName(): String = ""

    override fun getNewFragment(): Fragment =
            HotelHomepageFragment.getInstance()

    override fun shouldShowOptionMenu(): Boolean = true

    companion object {
        fun getCallingIntent(context: Context): Intent =
                Intent(context, HotelHomepageActivity::class.java)
    }
}

@DeepLink(ApplinkConstant.HOTEL, ApplinkConstant.HOTEL_DASHBOARD)
fun getCallingIntent(context: Context, extras: Bundle): Intent {
    val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
    val intent = Intent(context, HotelHomepageActivity::class.java)
    return intent.setData(uri.build())
            .putExtras(extras)
}