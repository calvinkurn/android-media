package com.tokopedia.product.detail.view.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.shop.BBInfo
import com.tokopedia.product.detail.data.model.shop.BlackBoxShipmentHolder
import com.tokopedia.product.detail.data.model.shop.ShopShipment
import com.tokopedia.product.detail.view.adapter.CourierTypeFactory
import kotlinx.android.synthetic.main.fragment_courier_list_detail.view.*

class CourierFragment: BaseListFragment<BlackBoxShipmentHolder, CourierTypeFactory>() {

    companion object {
        private const val ARGS_PRODUCT_LIST: String = "LIST_PRODUCT"
        private const val ARGS_BBINFO_LIST: String = "LIST_BBINFO"
        private const val ARGS_PRODUCT_ID: String = "ARGS_PRODUCT_ID"

        @JvmStatic
        fun newInstance(productId: String, shipments: List<ShopShipment>,
                        bbInfos: List<BBInfo>) = CourierFragment().apply {
            arguments = Bundle().also {
                it.putString(ARGS_PRODUCT_ID, productId)
                it.putParcelableArrayList(ARGS_PRODUCT_LIST, ArrayList(shipments))
                it.putParcelableArrayList(ARGS_BBINFO_LIST, ArrayList(bbInfos))
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
            val bbInfos: List<BBInfo> = it.getParcelableArrayList(ARGS_BBINFO_LIST) ?: listOf()
            val rv = getRecyclerView(view)
            if (bbInfos.isNotEmpty()){
                (activity as? BaseSimpleActivity)?.updateTitle(getString(R.string.product_detail_courier))
                super.renderList(bbInfos, false)
                view.title_bbinfo.visible()
                if (rv.itemDecorationCount > 0)
                    rv.removeItemDecorationAt(rv.itemDecorationCount - 1)
            } else {
                (activity as? BaseSimpleActivity)?.updateTitle(getString(R.string.courier_title))
                super.renderList(shipments, false)
                view.title_bbinfo.gone()
                rv.addItemDecoration(DividerItemDecoration(activity))
            }
        }
    }

    override fun getAdapterTypeFactory(): CourierTypeFactory = CourierTypeFactory()

    override fun onItemClicked(t: BlackBoxShipmentHolder?) {}

    override fun getScreenName(): String? = null

    override fun initInjector() {}

    override fun loadData(page: Int) {}
}