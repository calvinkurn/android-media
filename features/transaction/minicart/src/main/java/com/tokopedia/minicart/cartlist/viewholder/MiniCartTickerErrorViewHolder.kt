package com.tokopedia.minicart.cartlist.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartTickerErrorUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography

class MiniCartTickerErrorViewHolder(private val view: View,
                                    private val listener: MiniCartListActionListener)
    : AbstractViewHolder<MiniCartTickerErrorUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_ticker_error
    }

    private val tickerError: Ticker? by lazy {
        view.findViewById(R.id.ticker_error)
    }
    private val textShow: Typography? by lazy {
        view.findViewById(R.id.text_show)
    }

    override fun bind(element: MiniCartTickerErrorUiModel) {
        tickerError?.setHtmlDescription("Yaah, ada ${element.unavailableItemCount} barang tidak bisa diproses.")
        if (element.isShowErrorActionLabel) {
            textShow?.show()
        } else {
            textShow?.gone()
        }
    }

}