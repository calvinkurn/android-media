package com.tokopedia.seller.search.feature.suggestion.view.viewholder.navigation

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.indexOfSearchQuery
import com.tokopedia.seller.search.common.util.safeSetSpan
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.NavigationSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.NavigationSellerSearchUiModel
import kotlinx.android.synthetic.main.item_search_result_navigation.view.*

class ItemNavigationSearchViewHolder(
        itemViewNavigation: View,
        private val navigationSearchListener: NavigationSearchListener
) : AbstractViewHolder<NavigationSellerSearchUiModel>(itemViewNavigation) {

    companion object {
        val LAYOUT = R.layout.item_search_result_navigation
        const val SVG = ".svg"
    }

    override fun bind(element: NavigationSellerSearchUiModel) {
        bindTitleText(element)
        with(itemView) {
            if(element.imageUrl?.contains(SVG) == true) {
                ivSearchResultNav?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_topads))
            } else {
                ivSearchResultNav?.setImageUrl(element.imageUrl.orEmpty())
            }
            tvDescSearchResultNav?.text = element.desc
            setOnClickListener {
                navigationSearchListener.onNavigationItemClicked(element, adapterPosition)
            }
        }
    }

    private fun bindTitleText(item: NavigationSellerSearchUiModel) {
        val startIndex = indexOfSearchQuery(item.title.orEmpty(), item.keyword.orEmpty())
        with(itemView) {
            if (startIndex == -1) {
                tvTitleSearchResultNav?.text = item.title
            } else {
                val highlightedTitle = SpannableString(item.title)
                highlightedTitle.safeSetSpan(TextAppearanceSpan(context, R.style.searchTextHiglight),
                        0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                highlightedTitle.safeSetSpan(TextAppearanceSpan(context, R.style.searchTextHiglight),
                        startIndex + item.keyword?.length.orZero(),
                        item.title?.length.orZero(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                tvTitleSearchResultNav?.text = highlightedTitle
            }
        }
    }
}