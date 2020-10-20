package com.tokopedia.seller.search.feature.suggestion.view.viewholder.faq

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.indexOfSearchQuery
import com.tokopedia.seller.search.common.util.safeSetSpan
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.FaqSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.FaqSellerSearchUiModel
import kotlinx.android.synthetic.main.item_search_result_faq.view.*

class ItemFaqSearchViewHolder(
        itemFaqView: View,
        private val faqSearchListener: FaqSearchListener
): AbstractViewHolder<FaqSellerSearchUiModel>(itemFaqView) {

    companion object {
        val LAYOUT = R.layout.item_search_result_faq
    }

    override fun bind(element: FaqSellerSearchUiModel) {
        bindTitleText(element)

        itemView.setOnClickListener {
            faqSearchListener.onFaqItemClicked(element, adapterPosition)
        }
    }

    private fun bindTitleText(item: FaqSellerSearchUiModel) {
        val startIndex = indexOfSearchQuery(item.title.orEmpty(), item.keyword.orEmpty())
        with(itemView) {
            if (startIndex == -1) {
                tvTitleSearchResultFaq?.text = item.title
            } else {
                val highlightedTitle = SpannableString(item.title)
                highlightedTitle.safeSetSpan(TextAppearanceSpan(context, R.style.searchTextHiglight),
                        0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                highlightedTitle.safeSetSpan(TextAppearanceSpan(context, R.style.searchTextHiglight),
                        startIndex + item.keyword?.length.orZero(),
                        item.title?.length.orZero(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                tvTitleSearchResultFaq?.text = highlightedTitle
            }
        }
    }
}