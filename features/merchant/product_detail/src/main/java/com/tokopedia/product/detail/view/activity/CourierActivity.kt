package com.tokopedia.product.detail.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.detail.data.model.shop.BBInfo
import com.tokopedia.product.detail.data.model.shop.ShopShipment
import com.tokopedia.product.detail.view.fragment.CourierFragment

class CourierActivity: BaseSimpleActivity() {
    companion object {
        private const val ARGS_LIST_SHIPMENT: String = "LIST_SHIPMENT"
        private const val ARGS_LIST_BBINFO: String = "LIST_BBINFO"
        private const val ARGS_PRODUCT_ID: String = "ARGS_PRODUCT_ID"

        @JvmStatic
        fun createIntent(context: Context, productId: String, shipments: List<ShopShipment>,
                         bbInfos: List<BBInfo>) =
                Intent(context, CourierActivity::class.java)
                        .putExtra(ARGS_PRODUCT_ID, productId)
                        .putParcelableArrayListExtra(ARGS_LIST_SHIPMENT, ArrayList(shipments))
                        .putParcelableArrayListExtra(ARGS_LIST_BBINFO, ArrayList(bbInfos))
    }

    override fun getNewFragment(): Fragment = CourierFragment.newInstance(
            intent.getStringExtra(ARGS_PRODUCT_ID), intent.getParcelableArrayListExtra(ARGS_LIST_SHIPMENT) ?: listOf(),
            intent.getParcelableArrayListExtra(ARGS_LIST_BBINFO) ?: listOf()
    )
}