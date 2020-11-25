package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchMinCharUiModel

class SellerSearchMinCharViewHolder(view: View): AbstractViewHolder<SellerSearchMinCharUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.initial_search_min_character_state
    }

    override fun bind(element: SellerSearchMinCharUiModel) {}
}