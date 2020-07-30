package com.tokopedia.seller.search.feature.suggestion.view.viewholder.order

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ORDER
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.OrderSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.SellerSearchUiModel
import kotlinx.android.synthetic.main.search_result_order.view.*

class OrderSearchViewHolder(private val view: View,
                            private val orderSearchListener: OrderSearchListener) : AbstractViewHolder<SellerSearchUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.search_result_order
    }

    private val adapterOrder by lazy { ItemOrderSearchAdapter(orderSearchListener) }

    override fun bind(element: SellerSearchUiModel) {
        with(view) {
            element.takeIf { it.id == ORDER }?.let { order ->
                if (order.hasMore) {
                    tvMoreResultOrder?.apply {
                        show()
                        text = order.actionTitle.orEmpty()
                        setOnClickListener {
                            orderSearchListener.onOrderMoreClicked(order, adapterPosition)
                        }
                    }
                } else {
                    tvMoreResultOrder?.hide()
                }
                tvTitleResultOrder?.text = order.title
                rvResultOrder?.apply {
                    layoutManager = LinearLayoutManager(view.context)
                    adapter = adapterOrder
                }
                if (adapterPosition == element.count.orZero() - 1) {
                    dividerOrder?.hide()
                }

                if (order.sellerSearchList.isNotEmpty()) {
                    adapterOrder.submitList(order.sellerSearchList)
                }
            }
        }
    }
}