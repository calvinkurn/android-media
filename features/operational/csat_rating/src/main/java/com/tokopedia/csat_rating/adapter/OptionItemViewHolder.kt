package com.tokopedia.csat_rating.adapter

import android.graphics.drawable.GradientDrawable
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.csat_rating.R
import com.tokopedia.abstraction.R as RAbstraction
import com.tokopedia.csat_rating.quickfilter.ItemFilterViewHolder
import com.tokopedia.csat_rating.quickfilter.QuickFilterItem
import com.tokopedia.csat_rating.quickfilter.QuickSingleFilterListener

open class OptionItemViewHolder(itemView: View, listener: QuickSingleFilterListener?) : ItemFilterViewHolder(itemView, listener) {

    override fun updateData(filterItem: QuickFilterItem) {
        filterName?.text = filterItem.name
        layoutBorder?.setBackgroundResource(R.drawable.rounded_stroke_grey)
        layoutInside?.setBackgroundResource(R.drawable.rounded_stroke_grey)
    }

    override fun updateItemColor(selected: Boolean) {
        val drawableInside = layoutInside?.background?.current?.mutate() as GradientDrawable
        val drawableBorder = layoutBorder?.background?.current?.mutate() as GradientDrawable
        if (selected) {
            layoutInside?.context?.let { drawableBorder.setColor(MethodChecker.getColor(it, RAbstraction.color.tkpd_main_green)) }
            layoutBorder?.context?.let { drawableInside.setColor(MethodChecker.getColor(it, RAbstraction.color.light_green)) }
            filterName?.context?.let { filterName?.setTextColor(MethodChecker.getColor(it, RAbstraction.color.font_black_primary_70)) }
        } else {
            layoutInside?.context?.let { drawableBorder.setColor(MethodChecker.getColor(it, RAbstraction.color.grey_500)) }
            layoutBorder?.context?.let { drawableInside.setColor(MethodChecker.getColor(it, RAbstraction.color.Unify_N0)) }
            filterName?.context?.let { filterName?.setTextColor(MethodChecker.getColor(it, RAbstraction.color.grey_500)) }
        }
    }


}
