package com.tokopedia.minicart.cartlist.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartTickerWarningUiModel

class MiniCartTickerWarningViewHolder(private val view: View,
                                      private val listener: MiniCartListActionListener)
    : AbstractViewHolder<MiniCartTickerWarningUiModel>(view){

    companion object {
        val LAYOUT = R.layout.item_mini_cart_ticker_warning
    }

    override fun bind(element: MiniCartTickerWarningUiModel) {

    }

}