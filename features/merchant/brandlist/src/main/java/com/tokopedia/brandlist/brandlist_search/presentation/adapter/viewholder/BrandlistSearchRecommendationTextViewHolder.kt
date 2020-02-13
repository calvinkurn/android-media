package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchRecommendationTextViewModel
import kotlinx.android.synthetic.main.brandlist_recommended_text_item.view.*

class BrandlistSearchRecommendationTextViewHolder(view: View) : AbstractViewHolder<BrandlistSearchRecommendationTextViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.brandlist_recommended_text_item
    }

    override fun bind(element: BrandlistSearchRecommendationTextViewModel?) {
        itemView.rekomendasi_header.visibility = View.VISIBLE
    }
}