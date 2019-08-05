package com.tokopedia.csat_rating.adapter

import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.view.View

import com.tokopedia.csat_rating.R
import com.tokopedia.design.quickfilter.ItemFilterViewHolder
import com.tokopedia.design.quickfilter.QuickFilterItem
import com.tokopedia.design.quickfilter.QuickSingleFilterListener

class OptionItemViewHolder(itemView: View, listener: QuickSingleFilterListener) : ItemFilterViewHolder(itemView, listener) {

    override fun updateData(filterItem: QuickFilterItem) {
        filterName.text = filterItem.name
        layoutBorder.setBackgroundResource(R.drawable.rounded_stroke_grey)
        layoutInside.setBackgroundResource(R.drawable.rounded_stroke_grey)
    }

    override fun updateItemColor(selected: Boolean) {
        val drawableInside = layoutInside.background.current.mutate() as GradientDrawable
        val drawableBorder = layoutBorder.background.current.mutate() as GradientDrawable
        if (selected) {
            drawableBorder.setColor(ContextCompat.getColor(layoutInside.context, com.tokopedia.design.R.color.tkpd_main_green))
            drawableInside.setColor(ContextCompat.getColor(layoutBorder.context, com.tokopedia.design.R.color.light_green))
            filterName.setTextColor(ContextCompat.getColor(filterName.context, com.tokopedia.design.R.color.font_black_primary_70))
        } else {
            drawableBorder.setColor(ContextCompat.getColor(layoutInside.context, com.tokopedia.design.R.color.grey_500))
            drawableInside.setColor(ContextCompat.getColor(layoutBorder.context, com.tokopedia.design.R.color.white))
            filterName.setTextColor(ContextCompat.getColor(filterName.context, com.tokopedia.design.R.color.grey_500))
        }
    }


}
