package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomember_seller_dashboard.model.ColorTemplateListItem
import kotlinx.android.synthetic.main.tm_dash_color_item.view.*

class TokomemberDashCardColorVh(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(colorTemplateListItem: ColorTemplateListItem) {
        itemView.colorFrame.setBackgroundColor(Color.parseColor(colorTemplateListItem.colorCode))
    }
}