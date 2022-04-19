package com.tokopedia.chatbot.view.customview.csat.adapter

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chatbot.R
import com.tokopedia.csat_rating.adapter.OptionItemViewHolder
import com.tokopedia.csat_rating.quickfilter.QuickFilterItem
import com.tokopedia.csat_rating.quickfilter.QuickSingleFilterListener

class ChatBotOptionItemViewHolder(itemView: View, listener: QuickSingleFilterListener?) : OptionItemViewHolder(itemView, listener) {

    private var layoutInside1: ImageView = itemView.findViewById(R.id.layout_inside1)

    override fun updateData(filterItem: QuickFilterItem) {
        filterName?.text = filterItem.name
        filterName?.context?.let {
            filterName?.setTextColor(MethodChecker.getColor(it, com.tokopedia.abstraction.R.color.font_black_primary_70))
        }

        layoutInside1.setBackgroundResource(R.drawable.ic_csat_check_box_bg)
    }

    override fun updateItemColor(selected: Boolean) {
        if (selected) {
            itemView.context?.let {
                MethodChecker.setBackground(layoutInside1, MethodChecker.getDrawable(it, R.drawable.ic_checked_csat_option))
            }
        } else {
            itemView.context?.let {
                MethodChecker.setBackground(layoutInside1, MethodChecker.getDrawable(it, R.drawable.ic_csat_check_box_bg))
            }
        }
    }

    init {
        layoutInside1 = itemView.findViewById(R.id.layout_inside1)
        filterName = itemView.findViewById(R.id.filter_name)
    }
}