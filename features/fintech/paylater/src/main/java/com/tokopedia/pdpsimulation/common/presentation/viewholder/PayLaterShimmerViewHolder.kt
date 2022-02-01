package com.tokopedia.pdpsimulation.common.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.SeeMoreOptionsUiModel
import com.tokopedia.pdpsimulation.paylater.domain.model.SupervisorUiModel

class PayLaterShimmerViewHolder(itemView: View) :
    AbstractViewHolder<LoadingModel>(itemView) {


    companion object {
        val LAYOUT = R.layout.paylater_product_list_shimmer_item
    }

    override fun bind(element: LoadingModel) {}
}
