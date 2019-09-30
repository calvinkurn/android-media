package com.tokopedia.shop.product.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.product.view.model.FreeOngkirTickerViewModel
import kotlinx.android.synthetic.main.item_shop_ticker_free_ongkir.view.ticker_free_ongkir

class FreeOngkirTickerViewHolder(
        itemView: View
) : AbstractViewHolder<FreeOngkirTickerViewModel>(itemView) {

    companion object {
        @JvmStatic
        @LayoutRes
        val LAYOUT = R.layout.item_shop_ticker_free_ongkir
    }

    override fun bind(element: FreeOngkirTickerViewModel) {
        with(itemView){
            ticker_free_ongkir.tickerTitle = element.title
            ticker_free_ongkir.setTextDescription(element.description)
        }
    }
}