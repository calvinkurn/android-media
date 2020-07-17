package com.tokopedia.topupbills.telco.prepaid.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topupbills.R

class TelcoProductShimmeringViewHolder(itemView: View) : AbstractViewHolder<LoadingModel>(itemView) {

    override fun bind(element: LoadingModel) {
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_shimmering_product_grid
    }
}
