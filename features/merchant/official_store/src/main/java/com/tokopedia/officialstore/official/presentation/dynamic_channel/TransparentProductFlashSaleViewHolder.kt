package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R

class TransparentProductFlashSaleViewHolder(
        view: View,
        private val listener: TransparentProductFlashSaleClickListener?
) : AbstractViewHolder<EmptyModel>(view) {

    companion object {
        val LAYOUT = R.layout.layout_product_card_carousel_transparent_item
    }

    override fun bind(element: EmptyModel?) {
        itemView.setOnClickListener { listener?.onClickTransparentItem() }
    }
}