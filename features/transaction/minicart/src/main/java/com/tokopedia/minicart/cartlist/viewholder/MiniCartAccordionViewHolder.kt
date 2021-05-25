package com.tokopedia.minicart.cartlist.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartAccordionUiModel

class MiniCartAccordionViewHolder(private val view: View,
                                  private val listener: MiniCartListActionListener)
    : AbstractViewHolder<MiniCartAccordionUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_accordion
    }

    override fun bind(element: MiniCartAccordionUiModel) {

    }

}