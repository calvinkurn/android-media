package com.tokopedia.seller.search.feature.suggestion.view.viewholder.order

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.CUSTOMER
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.INV
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.SHIPPING
import com.tokopedia.seller.search.common.util.indexOfSearchQuery
import com.tokopedia.seller.search.common.util.safeSetSpan
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.OrderSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ItemSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.OrderSellerSearchUiModel
import kotlinx.android.synthetic.main.item_search_result_order.view.*

class ItemOrderSearchViewHolder(
        itemViewOrder: View,
        private val orderSearchListener: OrderSearchListener
): AbstractViewHolder<OrderSellerSearchUiModel>(itemViewOrder) {

    companion object {
        val LAYOUT = R.layout.item_search_result_order
    }

    override fun bind(element: OrderSellerSearchUiModel) {
        with(itemView) {
            if(element.imageUrl?.isBlank() == true) {
                when (element.id) {
                    INV, SHIPPING -> {
                        ivSearchResultOrder?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_invoice_seller_search))
                    }
                    CUSTOMER -> {
                        ivSearchResultOrder?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_buyers))
                    }
                }
            } else {
                ivSearchResultOrder?.setImageUrl(element.imageUrl.orEmpty())
            }

            bindTitleText(element)
            tvSearchResultOrderDesc?.text = element.desc

            setOnClickListener {
                orderSearchListener.onOrderItemClicked(element, adapterPosition)
            }
        }
    }

    private fun bindTitleText(item: ItemSellerSearchUiModel) {
        val startIndex = indexOfSearchQuery(item.title.orEmpty(), item.keyword.orEmpty())
        with(itemView) {
            if (startIndex == -1) {
                tvSearchResultOrderTitle?.text = item.title
            } else {
                val highlightedTitle = SpannableString(item.title)
                highlightedTitle.safeSetSpan(TextAppearanceSpan(context, R.style.searchTextHiglight),
                        0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                highlightedTitle.safeSetSpan(TextAppearanceSpan(context, R.style.searchTextHiglight),
                        startIndex + item.keyword?.length.orZero(),
                        item.title?.length.orZero(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                tvSearchResultOrderTitle?.text = highlightedTitle
            }
        }
    }
}