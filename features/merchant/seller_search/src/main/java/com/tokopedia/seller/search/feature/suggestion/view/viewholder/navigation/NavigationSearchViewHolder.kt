package com.tokopedia.seller.search.feature.suggestion.view.viewholder.navigation

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.NAVIGATION
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.NavigationSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.SellerSearchUiModel
import kotlinx.android.synthetic.main.search_result_navigation.view.*

class NavigationSearchViewHolder(private val view: View,
                                 private val navigationSearchListener: NavigationSearchListener) : AbstractViewHolder<SellerSearchUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.search_result_navigation
    }

    private var adapterNavigation: ItemNavigationSearchAdapter? = null

    override fun bind(element: SellerSearchUiModel) {
        adapterNavigation = ItemNavigationSearchAdapter(navigationSearchListener)
        with(itemView) {
            element.takeIf { it.id == NAVIGATION }?.let { feature ->
                tvTitleResultNavigation?.text = feature.title
                rvResultNavigation?.apply {
                    layoutManager = LinearLayoutManager(view.context)
                    adapter = adapterNavigation
                }

                if (adapterPosition == element.count.orZero() - 1) {
                    dividerNavigation?.hide()
                }

                if (feature.sellerSearchList.isNotEmpty()) {
                    adapterNavigation?.submitList(feature.sellerSearchList)
                }
            }
        }
    }
}