package com.tokopedia.minicart.cartlist.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartTickerWarningUiModel
import com.tokopedia.unifycomponents.ticker.Ticker

class MiniCartTickerWarningViewHolder(private val view: View,
                                      private val listener: MiniCartListActionListener)
    : AbstractViewHolder<MiniCartTickerWarningUiModel>(view){

    companion object {
        val LAYOUT = R.layout.item_mini_cart_ticker_warning
    }

    private val tickerInformation: Ticker? by lazy {
        view.findViewById(R.id.ticker_information)
    }

    override fun bind(element: MiniCartTickerWarningUiModel) {
        tickerInformation?.setHtmlDescription(element.warningMessage)
    }

}