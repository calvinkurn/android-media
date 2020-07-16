package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.StockTickerInfoUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.item_campaign_stock_ticker_info.view.*

class StockTickerInfoViewHolder(itemView: View?): AbstractViewHolder<StockTickerInfoUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_ticker_info
    }

    override fun bind(element: StockTickerInfoUiModel) {
        with(itemView) {
            ticker_campaign_stock?.run {
                tickerType =
                        if (element.isReserved) {
                            Ticker.TYPE_ANNOUNCEMENT
                        } else {
                            Ticker.TYPE_WARNING
                        }
                setTextDescription(
                        if (element.isReserved) {
                            context?.getString(R.string.product_manage_campaign_stock_open_campaign).orEmpty()
                        } else {
                            context?.getString(R.string.product_manage_campaign_stock_variant_zero_desc).orEmpty()
                        }
                )
            }
        }
    }
}