package com.tokopedia.hotel.orderdetail.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.orderdetail.di.DaggerHotelOrderDetailComponent
import com.tokopedia.hotel.orderdetail.di.HotelOrderDetailComponent
import com.tokopedia.hotel.orderdetail.presentation.fragment.HotelOrderDetailFragment

/**
 * @author by jessica on 10/05/19
 */

class HotelOrderDetailActivity : HotelBaseActivity(), HasComponent<HotelOrderDetailComponent> {

    override fun getParentViewResourceID() = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

    lateinit var orderId: String

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment =
            HotelOrderDetailFragment.getInstance(orderId, HOTEL_ORDER_CATEGORY)

    override fun getComponent(): HotelOrderDetailComponent =
            DaggerHotelOrderDetailComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            orderId = uri.lastPathSegment ?: ""
        } else if (intent.extras?.containsKey(KEY_ORDER_ID) == true) {
            orderId = intent.getStringExtra(KEY_ORDER_ID) ?: ""
        }

        super.onCreate(savedInstanceState)
    }

    companion object {
        fun getCallingIntent(context: Context,
                             orderId: String): Intent =
                Intent(context, HotelOrderDetailActivity::class.java)
                        .putExtra(KEY_ORDER_ID, orderId)

        const val HOTEL_ORDER_CATEGORY = "HOTELS"

        const val KEY_ORDER_ID = "OrderId"
        const val KEY_ORDER_CATEGORY = "OrderCategory"
    }
}
