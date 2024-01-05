package com.tokopedia.deals.ui.search.ui.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.ui.search.listener.DealsSearchListener
import com.tokopedia.deals.ui.search.model.visitor.MoreBrandModel

class MoreBrandViewHolder (itemView: View, private val searchListener: DealsSearchListener): AbstractViewHolder<MoreBrandModel>(itemView) {

    private val buttonMoreBrand: TextView = itemView.findViewById(R.id.btn_more_brands)

    override fun bind(element: MoreBrandModel) {
        buttonMoreBrand.text = String.format(getString(R.string.deals_brand_count_text),
                element.searchQuery,
                element.size)
        searchListener.onMoreBrandClicked(itemView, element)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_deals_more_brand
    }
}
