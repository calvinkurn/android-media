package com.tokopedia.product.detail.view.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.product.detail.data.model.shop.ShopShipment
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.view.adapter.CourierTypeFactory

class CourierFragment: BaseListFragment<ShopShipment, CourierTypeFactory>() {

    private val productDetailTracking: ProductDetailTracking by lazy {
        ProductDetailTracking((context?.applicationContext as? AbstractionRouter)?.analyticTracker)
    }

    companion object {
        private const val ARGS_LIST: String = "ARGS_LIST"
        private const val ARGS_PRODUCT_ID: String = "ARGS_PRODUCT_ID"

        @JvmStatic
        fun newInstance(productId: String, shipments: List<ShopShipment>) = CourierFragment().apply {
            arguments = Bundle().also {
                it.putString(ARGS_PRODUCT_ID, productId)
                it.putParcelableArrayList(ARGS_LIST, ArrayList(shipments))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.resources?.configuration?.let { setUpByConfiguration(it) }
        arguments?.let {
            val shipments: List<ShopShipment> = it.getParcelableArrayList(ARGS_LIST)
            super.renderList(shipments, false)
        }
    }

    private fun setUpByConfiguration(configuration: Configuration) {}

    override fun getAdapterTypeFactory(): CourierTypeFactory = CourierTypeFactory()

    override fun onItemClicked(t: ShopShipment?) {}

    override fun getScreenName(): String? = null

    override fun initInjector() {}

    override fun loadData(page: Int) {}
}