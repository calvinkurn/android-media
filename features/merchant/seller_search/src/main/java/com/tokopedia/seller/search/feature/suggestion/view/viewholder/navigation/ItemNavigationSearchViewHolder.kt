package com.tokopedia.seller.search.feature.suggestion.view.viewholder.navigation

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.bindTitleText
import com.tokopedia.seller.search.databinding.ItemSearchResultNavigationBinding
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.NavigationSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.adapter.SubItemNavigationSearchAdapter
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.NavigationSellerSearchUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemNavigationSearchViewHolder(
    itemViewNavigation: View,
    private val navigationSearchListener: NavigationSearchListener
) : AbstractViewHolder<NavigationSellerSearchUiModel>(itemViewNavigation) {

    companion object {
        val LAYOUT = R.layout.item_search_result_navigation
        const val SVG = ".svg"
    }

    private val binding: ItemSearchResultNavigationBinding? by viewBinding()

    override fun bind(element: NavigationSellerSearchUiModel) {
        binding?.run {
            tvTitleSearchResultNav.bindTitleText(element.title.orEmpty(), element.keyword.orEmpty())
            if (element.imageUrl?.contains(SVG) == true) {
                ivSearchResultNav.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context,
                        R.drawable.ic_topads
                    )
                )
            } else {
                ivSearchResultNav.setImageUrl(element.imageUrl.orEmpty())
            }
            tvDescSearchResultNav.text = element.desc
            root.setOnClickListener {
                navigationSearchListener.onNavigationItemClicked(element, adapterPosition)
            }
            setupSubItems(element)
        }
    }

    private fun setupSubItems(element: NavigationSellerSearchUiModel) {
        binding?.run {
            rvSearchSubItem.isVisible = element.subItems.isNotEmpty()
            rvSearchSubItem.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
            rvSearchSubItem.adapter = SubItemNavigationSearchAdapter(element.subItems)
        }
    }
}