package com.tokopedia.seller.search.feature.suggestion.view.viewholder.order

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.CUSTOMER
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.INV
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.SHIPPING
import com.tokopedia.seller.search.common.util.bindTitleText
import com.tokopedia.seller.search.databinding.ItemSearchResultOrderBinding
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.OrderSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.OrderSellerSearchUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemOrderSearchViewHolder(
    itemViewOrder: View,
    private val orderSearchListener: OrderSearchListener
) : AbstractViewHolder<OrderSellerSearchUiModel>(itemViewOrder) {

    companion object {
        val LAYOUT = R.layout.item_search_result_order
    }

    private val binding: ItemSearchResultOrderBinding? by viewBinding()

    override fun bind(element: OrderSellerSearchUiModel) {
        binding?.run {
            tvSearchResultOrderTitle.bindTitleText(
                element.title.orEmpty(),
                element.keyword.orEmpty()
            )
            tvSearchResultOrderDesc.text = element.desc

            root.setOnClickListener {
                orderSearchListener.onOrderItemClicked(element, adapterPosition)
            }
        }
    }
}
