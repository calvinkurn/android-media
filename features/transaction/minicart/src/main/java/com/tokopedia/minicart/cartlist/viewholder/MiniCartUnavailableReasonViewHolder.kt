package com.tokopedia.minicart.cartlist.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUnavailableReasonUiModel

class MiniCartUnavailableReasonViewHolder(private val view: View,
                                          private val listener: MiniCartListActionListener)
    : AbstractViewHolder<MiniCartUnavailableReasonUiModel>(view){

    companion object {
        val LAYOUT = R.layout.item_mini_cart_unavailable_reason
    }

    override fun bind(element: MiniCartUnavailableReasonUiModel) {

    }

}