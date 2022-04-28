package com.tokopedia.entertainment.home.adapter.viewholder

import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.viewmodel.TickerModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback

/**
 * created by @bayazidnasir on 26/4/2022
 */

class TickerViewHolder(itemView: View): AbstractViewHolder<TickerModel>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.ent_layout_viewholder_event_ticker
    }

    override fun bind(element: TickerModel) {
        val ticker = itemView.findViewById<Ticker>(R.id.ticker_view)
        ticker.setTextDescription(element.message)
    }
}