package com.tokopedia.product.detail.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.shop.BlackBoxShipmentHolder
import com.tokopedia.product.detail.data.model.shop.ProductShopShipment
import com.tokopedia.product.detail.view.adapter.CourierTypeFactory
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopShipment

class CourierFragment: BaseListFragment<BlackBoxShipmentHolder, CourierTypeFactory>() {

    companion object {
        private const val ARGS_PRODUCT_LIST: String = "LIST_PRODUCT"
        private const val ARGS_PRODUCT_ID: String = "ARGS_PRODUCT_ID"

        @JvmStatic
        fun newInstance(productId: String, shipments: List<ShopShipment>) = CourierFragment().apply {
            arguments = Bundle().also {
                it.putString(ARGS_PRODUCT_ID, productId)
                it.putParcelableArrayList(ARGS_PRODUCT_LIST, ArrayList(shipments))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_courier_list_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val shipments: List<ShopShipment> = it.getParcelableArrayList(ARGS_PRODUCT_LIST) ?: listOf()
            val shopShipments = shipments.map { shipment ->
                ProductShopShipment(shipment.isAvailable,
                        shipment.code, shipment.shipmentID, shipment.image, shipment.name, shipment.isPickup,
                        shipment.maxAddFee, shipment.awbStatus, shipment.product)
            }
            val rv = getRecyclerView(view)
            (activity as? BaseSimpleActivity)?.updateTitle(getString(R.string.courier_title))
            super.renderList(shopShipments, false)
            rv?.addItemDecoration(DividerItemDecoration(activity))
        }
    }

    override fun getAdapterTypeFactory(): CourierTypeFactory = CourierTypeFactory()

    override fun onItemClicked(t: BlackBoxShipmentHolder?) {}

    override fun getScreenName(): String? = null

    override fun initInjector() {}

    override fun loadData(page: Int) {}
}