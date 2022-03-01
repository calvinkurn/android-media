package com.tokopedia.pdpsimulation.paylater.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.pdpsimulation.R

class PayLaterShimmerViewHolder(itemView: View) :
        AbstractViewHolder<LoadingModel>(itemView) {


    companion object {
        val LAYOUT = R.layout.paylater_product_list_shimmer_item
    }

    override fun bind(element: LoadingModel) {}
}
