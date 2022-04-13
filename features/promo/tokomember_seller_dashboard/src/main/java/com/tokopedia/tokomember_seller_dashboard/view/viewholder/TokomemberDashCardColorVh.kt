package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.graphics.Color
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberProgramColorItem
import kotlinx.android.synthetic.main.tm_dash_color_item.view.*

class TokomemberDashCardColorVh(val view: View)
    : AbstractViewHolder<TokomemberProgramColorItem>(view) {

    private val tmProgramColorFrame = itemView.colorFrame

    override fun bind(element: TokomemberProgramColorItem?) {
        element?.apply {
            tmProgramColorFrame.setBackgroundColor(Color.parseColor(colorCode))
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.tm_dash_color_item
    }
}