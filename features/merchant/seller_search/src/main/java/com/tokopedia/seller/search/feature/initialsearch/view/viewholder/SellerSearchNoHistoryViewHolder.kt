package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoHistoryUiModel

class SellerSearchNoHistoryViewHolder(view: View): AbstractViewHolder<SellerSearchNoHistoryUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.initial_search_no_history
    }

    override fun bind(element: SellerSearchNoHistoryUiModel) {}
}