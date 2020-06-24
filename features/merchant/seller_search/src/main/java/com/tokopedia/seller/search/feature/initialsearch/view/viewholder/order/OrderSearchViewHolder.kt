package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.order

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ORDER
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.SellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.OrderSearchListener
import kotlinx.android.synthetic.main.search_result_order.view.*

class OrderSearchViewHolder(private val view: View,
                            private val orderSearchListener: OrderSearchListener) : AbstractViewHolder<SellerSearchUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.search_result_order
    }

    private val adapterOrder by lazy { ItemOrderSearchAdapter(orderSearchListener) }

    override fun bind(element: SellerSearchUiModel) {
        view.tvTitleResultOrder?.text = element.title
        view.rvResultOrder?.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = adapterOrder
        }

        if (adapterPosition == element.count.orZero() - 1) {
            view.dividerOrder?.hide()
        }

        if (element.sellerSearchList.isNotEmpty()) {
            adapterOrder.clearAllData()
            element.takeIf { it.id == ORDER }?.sellerSearchList?.let {
                adapterOrder.setItemOrderList(it)
            }
        }
    }
}