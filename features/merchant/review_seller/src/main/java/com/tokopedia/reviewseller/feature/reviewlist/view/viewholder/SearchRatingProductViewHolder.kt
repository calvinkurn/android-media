package com.tokopedia.reviewseller.feature.reviewlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewlist.view.model.SearchRatingProductUiModel

class SearchRatingProductViewHolder(view: View): AbstractViewHolder<SearchRatingProductUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_search_rating_product
    }

    override fun bind(element: SearchRatingProductUiModel) {}
}