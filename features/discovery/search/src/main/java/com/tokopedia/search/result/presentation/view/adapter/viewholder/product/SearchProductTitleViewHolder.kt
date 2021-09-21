package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.SearchProductTitleDataView
import kotlinx.android.synthetic.main.search_result_product_title_layout.view.*

class SearchProductTitleViewHolder(itemView: View): AbstractViewHolder<SearchProductTitleDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_product_title_layout
    }

    override fun bind(element: SearchProductTitleDataView) {
        itemView.searchProductTitle?.text = getTitle(element)
    }

    private fun getTitle(element: SearchProductTitleDataView) =
            if (element.isRecommendationTitle)
                getString(R.string.search_result_local_search_other_products, element.title)
            else element.title
}