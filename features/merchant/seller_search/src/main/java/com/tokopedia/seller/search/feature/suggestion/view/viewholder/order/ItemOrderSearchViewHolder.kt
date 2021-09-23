package com.tokopedia.seller.search.feature.suggestion.view.viewholder.order

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.CUSTOMER
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.INV
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.SHIPPING
import com.tokopedia.seller.search.common.util.bindTitleText
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.OrderSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.OrderSellerSearchUiModel
import kotlinx.android.synthetic.main.item_search_result_order.view.*

class ItemOrderSearchViewHolder(
    itemViewOrder: View,
    private val orderSearchListener: OrderSearchListener
) : AbstractViewHolder<OrderSellerSearchUiModel>(itemViewOrder) {

    companion object {
        val LAYOUT = R.layout.item_search_result_order
    }

    override fun bind(element: OrderSellerSearchUiModel) {
        with(itemView) {
            if (element.imageUrl?.isBlank() == true) {
                when (element.id) {
                    INV, SHIPPING -> {
                        ivSearchResultOrder?.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_invoice_seller_search
                            )
                        )
                    }
                    CUSTOMER -> {
                        ivSearchResultOrder?.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_buyers
                            )
                        )
                    }
                }
            } else {
                ivSearchResultOrder?.setImageUrl(element.imageUrl.orEmpty())
            }

            tvSearchResultOrderTitle?.bindTitleText(
                element.title.orEmpty(),
                element.keyword.orEmpty()
            )
            tvSearchResultOrderDesc?.text = element.desc

            setOnClickListener {
                orderSearchListener.onOrderItemClicked(element, adapterPosition)
            }
        }
    }
}