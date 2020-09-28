package com.tokopedia.product.detail.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.detail.view.fragment.CourierFragment
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopShipment

class CourierActivity: BaseSimpleActivity() {
    companion object {
        private const val ARGS_LIST_SHIPMENT: String = "LIST_SHIPMENT"
        private const val ARGS_PRODUCT_ID: String = "ARGS_PRODUCT_ID"

        @JvmStatic
        fun createIntent(context: Context, productId: String, shipments: List<ShopShipment>) =
                Intent(context, CourierActivity::class.java)
                        .putExtra(ARGS_PRODUCT_ID, productId)
                        .putParcelableArrayListExtra(ARGS_LIST_SHIPMENT, ArrayList(shipments))
    }

    override fun getNewFragment(): Fragment = CourierFragment.newInstance(
            intent.getStringExtra(ARGS_PRODUCT_ID), intent.getParcelableArrayListExtra(ARGS_LIST_SHIPMENT) ?: listOf())
}