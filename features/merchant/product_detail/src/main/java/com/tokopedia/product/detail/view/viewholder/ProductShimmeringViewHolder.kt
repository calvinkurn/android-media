package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R

class ProductShimmeringViewHolder(val view: View) : AbstractViewHolder<LoadingModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_shimmering
    }

    override fun bind(element: LoadingModel?) {

    }
}