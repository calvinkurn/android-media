package com.tokopedia.hotel.orderdetail.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.orderdetail.di.DaggerHotelOrderDetailComponent
import com.tokopedia.hotel.orderdetail.di.HotelOrderDetailComponent
import com.tokopedia.hotel.orderdetail.presentation.fragment.HotelOrderDetailFragment

/**
 * @author by jessica on 10/05/19
 */

class HotelOrderDetailActivity: HotelBaseActivity(), HasComponent<HotelOrderDetailComponent> {

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment = HotelOrderDetailFragment.getInstance()

    override fun getComponent(): HotelOrderDetailComponent =
            DaggerHotelOrderDetailComponent.builder()
            .hotelComponent(HotelComponentInstance.getHotelComponent(application))
            .build()

    companion object {
        fun getCallingIntent(context: Context): Intent =
                Intent(context, HotelOrderDetailActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.hotel_order_detail_title))
    }

}

//@DeepLink(ApplinkConstant.HOTEL)
//fun getCallingIntent(context: Context, extras: Bundle): Intent {
//    val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
//    val intent = Intent(context, HotelHomepageActivity::class.java)
//    return intent.setData(uri.build())
//            .putExtras(extras)
//}