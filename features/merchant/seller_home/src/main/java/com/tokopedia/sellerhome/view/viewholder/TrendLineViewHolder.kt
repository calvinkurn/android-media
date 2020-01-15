package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.TrendLineViewUiModel
import kotlinx.android.synthetic.main.trend_line_layout.view.*

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

class TrendLineViewHolder(view: View?) : AbstractViewHolder<TrendLineViewUiModel>(view) {

    companion object {
        val RES_LAYOUT: Int = R.layout.trend_line_layout
    }

    override fun bind(element: TrendLineViewUiModel?) {
        with(itemView) {
            tvTrendLineHello.text = element?.someField
        }
    }
}