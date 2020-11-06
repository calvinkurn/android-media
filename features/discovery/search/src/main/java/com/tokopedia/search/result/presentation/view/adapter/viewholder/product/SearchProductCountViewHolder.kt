package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.SearchProductCountViewModel
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationClickListener
import kotlinx.android.synthetic.main.search_result_product_count_layout.view.*

class SearchProductCountViewHolder(itemView: View, private val listener: SearchNavigationClickListener): AbstractViewHolder<SearchProductCountViewModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_product_count_layout
    }

    override fun bind(element: SearchProductCountViewModel) {
        bindCountTitle(element)
        bindChangeViewListener(element)
    }

    private fun bindCountTitle(element: SearchProductCountViewModel) {
        itemView.searchProductCountTitle?.shouldShowWithAction(element.productCountString.isNotEmpty()) {
            val countTitle = "<b>" + element.productCountString + "</b>" + " hasil pencarian"
            itemView.searchProductCountTitle?.text = MethodChecker.fromHtml(countTitle)
        }
    }

    private fun bindChangeViewListener(element: SearchProductCountViewModel) {
        itemView.searchProductCountChangeViewButton?.setOnClickListener {
            listener.onChangeViewClicked(element.position)
        }
    }

    override fun bind(element: SearchProductCountViewModel?, payloads: MutableList<Any>) {
        payloads.getOrNull(0) ?: return

        val payload = payloads.getOrNull(0)
        when(payload) {
            SearchConstant.RecyclerView.VIEW_LIST -> changeViewButton(R.drawable.search_ic_view_list)
            SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID -> changeViewButton(R.drawable.search_ic_view_grid)
            SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID -> changeViewButton(R.drawable.search_ic_view_grid_big)
        }
    }

    private fun changeViewButton(id: Int) {
        ImageHandler.loadImageWithId(itemView.searchProductCountChangeViewButton, id)
    }
}