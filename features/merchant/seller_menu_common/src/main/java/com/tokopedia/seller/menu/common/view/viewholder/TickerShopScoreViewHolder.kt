package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.view.uimodel.TickerShopScoreUiModel
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.item_seller_menu_ticker_shop_score.view.*

class TickerShopScoreViewHolder(itemView: View, private val tickerShopScoreListener: TickerShopScoreListener?): AbstractViewHolder<TickerShopScoreUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_seller_menu_ticker_shop_score
    }

    override fun bind(element: TickerShopScoreUiModel?) {
        with(itemView) {
            tickerShopScore.apply {
                tickerTitle = element?.tickerTitle
                setHtmlDescription(element?.descTitle.orEmpty())

                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        tickerShopScoreListener?.onDescriptionViewClick()
                    }

                    override fun onDismiss() {}
                })
            }
        }
    }

    interface TickerShopScoreListener {
        fun onDescriptionViewClick()
    }
}