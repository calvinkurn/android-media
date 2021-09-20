package com.tokopedia.seller.search.feature.suggestion.view.viewholder.faq

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.bindTitleText
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.FaqSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.FaqSellerSearchUiModel
import kotlinx.android.synthetic.main.item_search_result_faq.view.*

class ItemFaqSearchViewHolder(
    itemFaqView: View,
    private val faqSearchListener: FaqSearchListener
) : AbstractViewHolder<FaqSellerSearchUiModel>(itemFaqView) {

    companion object {
        val LAYOUT = R.layout.item_search_result_faq
    }

    override fun bind(element: FaqSellerSearchUiModel) {
        itemView.tvTitleSearchResultFaq.bindTitleText(
            element.title.orEmpty(),
            element.keyword.orEmpty()
        )
        itemView.setOnClickListener {
            faqSearchListener.onFaqItemClicked(element, adapterPosition)
        }
    }
}