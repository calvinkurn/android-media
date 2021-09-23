package com.tokopedia.seller.search.feature.suggestion.view.viewholder.navigation

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.bindTitleText
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
        with(itemView) {
            tvTitleSearchResultNav.bindTitleText(element.title.orEmpty(), element.keyword.orEmpty())
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
}