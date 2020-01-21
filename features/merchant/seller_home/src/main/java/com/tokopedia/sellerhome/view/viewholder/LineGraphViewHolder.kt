package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.LineGraphWidgetUiModel
import kotlinx.android.synthetic.main.seller_home_line_graph_widget.view.*

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

class LineGraphViewHolder(view: View?) : AbstractViewHolder<LineGraphWidgetUiModel>(view) {

    companion object {
        val RES_LAYOUT: Int = R.layout.seller_home_line_graph_widget
    }

    override fun bind(element: LineGraphWidgetUiModel) {
        with(itemView) {
            tvTrendLineHello.text = element.title
        }
    }
}