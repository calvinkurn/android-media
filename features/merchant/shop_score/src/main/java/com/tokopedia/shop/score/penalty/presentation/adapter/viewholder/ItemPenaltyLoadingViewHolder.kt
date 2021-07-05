package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R

class ItemPenaltyLoadingViewHolder(view: View): AbstractViewHolder<LoadingModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_shimmer_penalty
    }

    override fun bind(element: LoadingModel?) {}

}