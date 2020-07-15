package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.suggestion.view.model.SellerSearchNoResultUiModel

class SellerSearchNoResultViewHolder(view: View): AbstractViewHolder<SellerSearchNoResultUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.initial_search_no_result
    }

    override fun bind(element: SellerSearchNoResultUiModel) {}
}