package com.tokopedia.seller.search.feature.suggestion.view.viewholder.order

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.CUSTOMER
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.INV
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.SHIPPING
import com.tokopedia.seller.search.common.util.indexOfSearchQuery
import com.tokopedia.seller.search.common.util.safeSetSpan
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.OrderSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ItemSellerSearchUiModel
import kotlinx.android.synthetic.main.item_search_result_order.view.*

class ItemOrderSearchViewHolder(
        private val itemViewOrder: View,
        private val orderSearchListener: OrderSearchListener
) : RecyclerView.ViewHolder(itemViewOrder) {

    fun bind(itemSellerSearchUiModel: ItemSellerSearchUiModel) {
        with(itemViewOrder) {
            if(itemSellerSearchUiModel.imageUrl?.isBlank() == true) {
                when (itemSellerSearchUiModel.id) {
                    INV, SHIPPING -> {
                        ivSearchResultOrder?.setImageDrawable(ContextCompat.getDrawable(itemViewOrder.context, R.drawable.ic_invoice_seller_search))
                    }
                    CUSTOMER -> {
                        ivSearchResultOrder?.setImageDrawable(ContextCompat.getDrawable(itemViewOrder.context, R.drawable.ic_buyers))
                    }
                }
            } else {
                ivSearchResultOrder?.setImageUrl(itemSellerSearchUiModel.imageUrl.orEmpty())
            }

            bindTitleText(itemSellerSearchUiModel)
            tvSearchResultOrderDesc?.text = itemSellerSearchUiModel.desc

            setOnClickListener {
                orderSearchListener.onOrderItemClicked(itemSellerSearchUiModel, adapterPosition)
            }
        }
    }

    private fun bindTitleText(item: ItemSellerSearchUiModel) {
        val startIndex = indexOfSearchQuery(item.title.orEmpty(), item.keyword.orEmpty())
        if (startIndex == -1) {
            itemViewOrder.tvSearchResultOrderTitle?.text = item.title
        } else {
            val highlightedTitle = SpannableString(item.title)
            highlightedTitle.safeSetSpan(TextAppearanceSpan(itemViewOrder.context, R.style.searchTextHiglight),
                    0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            highlightedTitle.safeSetSpan(TextAppearanceSpan(itemViewOrder.context, R.style.searchTextHiglight),
                    startIndex + item.keyword?.length.orZero(),
                    item.title?.length.orZero(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            itemViewOrder.tvSearchResultOrderTitle?.text = highlightedTitle
        }
    }
}