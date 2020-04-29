package com.tokopedia.contactus.inboxticket2.view.customview.adapter

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.contactus.R
import com.tokopedia.csat_rating.adapter.OptionItemViewHolder
import com.tokopedia.design.quickfilter.QuickFilterItem
import com.tokopedia.design.quickfilter.QuickSingleFilterListener

class ContactUsOptionItemViewHolder(itemView: View, listener: QuickSingleFilterListener) : OptionItemViewHolder(itemView, listener) {

    private var layoutInside1: ImageView = itemView.findViewById(R.id.layout_inside1)

    override fun updateData(filterItem: QuickFilterItem) {
        filterName.text = filterItem.name
        layoutInside1.setBackgroundResource(R.drawable.check_box_bg)
    }

    override fun updateItemColor(selected: Boolean) {
        if (selected) {
            MethodChecker.setBackground(layoutInside1, MethodChecker.getDrawable(itemView.context, R.drawable.checked))
            filterName.setTextColor(MethodChecker.getColor(filterName.context, com.tokopedia.design.R.color.font_black_primary_70))
        } else {
            MethodChecker.setBackground(layoutInside1, MethodChecker.getDrawable(itemView.context, R.drawable.check_box_bg))
            filterName.setTextColor(MethodChecker.getColor(filterName.context, com.tokopedia.design.R.color.grey_500))
        }
    }

    init {
        layoutInside1 = itemView.findViewById(R.id.layout_inside1)
        filterName = itemView.findViewById(R.id.filter_name)
    }
}