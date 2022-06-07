package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberCardColorAdapterListener
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardColor
import kotlinx.android.synthetic.main.tm_dash_color_item.view.*

var selectedItemPosColor = -1
var lastItemSelectedPosColor = -1

class TmDashCardColorVh(val view: View, private val listener: TokomemberCardColorAdapterListener) :
    AbstractViewHolder<TokomemberCardColor>(view) {

    private val tmCardColorFrame = itemView.colorFrame
    private val tmCardColorFrameSelector = itemView.viewColorSelector

    override fun bind(element: TokomemberCardColor?) {

        element?.apply {
            tmCardColorFrame.background.setColorFilter(
                Color.parseColor(colorCode),
                PorterDuff.Mode.SRC_ATOP
            )
        }
        itemView.setOnClickListener {
            selectedItemPosColor = adapterPosition
            lastItemSelectedPosColor = if (lastItemSelectedPosColor == -1)
                selectedItemPosColor
            else {
                listener.onItemClickCardColorSelect(tokoCardItem = element,position = lastItemSelectedPosColor)
                selectedItemPosColor
            }
            listener.onItemClickCardColorSelect(tokoCardItem = element,position = selectedItemPosColor)
        }
        if (adapterPosition == selectedItemPosColor) {
            tmCardColorFrameSelector.show()
        } else {
            tmCardColorFrameSelector.hide()
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.tm_dash_color_item
    }
}