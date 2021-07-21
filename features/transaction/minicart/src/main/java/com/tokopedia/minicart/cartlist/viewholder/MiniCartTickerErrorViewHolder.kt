package com.tokopedia.minicart.cartlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartTickerErrorUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartTickerErrorBinding

class MiniCartTickerErrorViewHolder(private val viewBinding: ItemMiniCartTickerErrorBinding,
                                    private val listener: MiniCartListActionListener)
    : AbstractViewHolder<MiniCartTickerErrorUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_ticker_error
    }

    override fun bind(element: MiniCartTickerErrorUiModel) {
        with(viewBinding) {
            val message = tickerError.context?.getString(R.string.mini_cart_message_ticker_error, element.unavailableItemCount)
                    ?: ""
            tickerError.setHtmlDescription(message)
            if (element.isShowErrorActionLabel) {
                textShow.show()
            } else {
                textShow.gone()
            }
            textShow.setOnClickListener {
                listener.onShowUnavailableItemsCLicked()
            }
        }
    }

}