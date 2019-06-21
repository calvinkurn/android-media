package com.tokopedia.hotel.orderdetail.presentation.activity

import android.content.Context
import android.content.Intent
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

class HotelOrderDetailActivity : HotelBaseActivity(), HasComponent<HotelOrderDetailComponent> {

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment =
            with(intent) {
                HotelOrderDetailFragment.getInstance(getStringExtra(KEY_ORDER_ID),
                        getStringExtra(KEY_ORDER_CATEGORY))
            }

    override fun getComponent(): HotelOrderDetailComponent =
            DaggerHotelOrderDetailComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.hotel_order_detail_title))
    }

    companion object {
        fun getCallingIntent(context: Context,
                             orderId: String): Intent =
                Intent(context, HotelOrderDetailActivity::class.java)
                        .putExtra(KEY_ORDER_ID, orderId)
                        .putExtra(KEY_ORDER_CATEGORY, HOTEL_ORDER_CATEGORY)

        const val HOTEL_ORDER_CATEGORY = "HOTELS"

        val KEY_ORDER_ID = "OrderId"
        val KEY_ORDER_CATEGORY = "OrderCategory"
    }
}

/*
@DeepLink(ApplinkConstant.HOTEL_ORDER_DETAIL)
fun getCallingIntent(context: Context, extras: Bundle): Intent {
    val uri: Uri.Builder = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
    return HotelOrderDetailActivity.getCallingIntent(context,
            extras.getString("order_id"))
            .setData(uri.build())
}*/
