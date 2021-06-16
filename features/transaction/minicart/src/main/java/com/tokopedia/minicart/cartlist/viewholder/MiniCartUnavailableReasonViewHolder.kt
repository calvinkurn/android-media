package com.tokopedia.minicart.cartlist.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUnavailableReasonUiModel
import com.tokopedia.unifyprinciples.Typography

class MiniCartUnavailableReasonViewHolder(private val view: View,
                                          private val listener: MiniCartListActionListener)
    : AbstractViewHolder<MiniCartUnavailableReasonUiModel>(view){

    companion object {
        val LAYOUT = R.layout.item_mini_cart_unavailable_reason
    }

    private val textDisabledTitle: Typography? by lazy {
        view.findViewById(R.id.text_disabled_title)
    }

    override fun bind(element: MiniCartUnavailableReasonUiModel) {
        textDisabledTitle?.text = element.reason
    }

}