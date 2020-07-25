package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder

import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohUtils
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohTypeData
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohItemAdapter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import kotlinx.android.synthetic.main.uoh_list_item.view.*

/**
 * Created by fwidjaja on 25/07/20.
 */
class UohOrderListLoaderViewHolder(itemView: View) : UohItemAdapter.BaseViewHolder<UohTypeData>(itemView) {
    override fun bind(item: UohTypeData, position: Int) {
        if (item.dataObject is UohListOrder.Data.UohOrders.Order) {
            itemView.cl_data_product.gone()
            itemView.cl_loader.visible()
            itemView.loader_ic_uoh_vertical.type = LoaderUnify.TYPE_CIRCLE
            itemView.loader_tv_uoh_categories.type = LoaderUnify.TYPE_RECT
            itemView.loader_label_status_and_three_dots.type = LoaderUnify.TYPE_RECT
            itemView.loader_iv_uoh_product.type = LoaderUnify.TYPE_RECT
            itemView.loader_product_name.type = LoaderUnify.TYPE_RECT
            itemView.loader_product_desc.type = LoaderUnify.TYPE_RECT
            itemView.loader_label_total_harga.type = LoaderUnify.TYPE_RECT
            itemView.loader_value_total_harga.type = LoaderUnify.TYPE_RECT
        }
    }
}