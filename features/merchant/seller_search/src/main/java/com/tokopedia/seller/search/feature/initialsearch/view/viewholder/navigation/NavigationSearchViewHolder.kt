package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.navigation

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.SellerSearchUiModel
import kotlinx.android.synthetic.main.search_result_navigation.view.*

class NavigationSearchViewHolder(view: View): AbstractViewHolder<SellerSearchUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.search_result_navigation
    }

    private val adapterNavigation by lazy { ItemNavigationSearchAdapter() }

    override fun bind(element: SellerSearchUiModel) {
        with(itemView) {
            tvTitleResultNavigation?.text = element.title
            rvResultNavigation?.apply {
                layoutManager = LinearLayoutManager(itemView.context)
                adapter = adapterNavigation
            }

            if(adapterPosition == element.count.orZero() - 1) {
                dividerNavigation?.hide()
            }
        }

        if(element.sellerSearchList.isNotEmpty()) {
            adapterNavigation.submitList(element.sellerSearchList)
        }
    }
}