package com.tokopedia.minicart.ui.cartlist.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.ui.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.ui.cartlist.uimodel.MiniCartTickerErrorUiModel

class MiniCartTickerErrorViewHolder(private val view: View,
                                    private val listener: MiniCartListActionListener)
    : AbstractViewHolder<MiniCartTickerErrorUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_ticker_error
    }

    override fun bind(element: MiniCartTickerErrorUiModel) {

    }

}