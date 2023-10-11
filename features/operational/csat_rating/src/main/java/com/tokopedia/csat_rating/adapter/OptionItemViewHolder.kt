package com.tokopedia.csat_rating.adapter

import android.graphics.drawable.GradientDrawable
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.csat_rating.R
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
            layoutInside?.context?.let { drawableBorder.setColor(MethodChecker.getColor(it,
                com.tokopedia.unifyprinciples.R.color.Unify_GN400)) }
            layoutBorder?.context?.let { drawableInside.setColor(MethodChecker.getColor(it,
                R.color.csat_dms_reason_bg)) }
            filterName?.context?.let { filterName?.setTextColor(MethodChecker.getColor(it,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500)) }
        } else {
            layoutInside?.context?.let { drawableBorder.setColor(MethodChecker.getColor(it,
                com.tokopedia.unifyprinciples.R.color.Unify_NN300)) }
            layoutBorder?.context?.let { drawableInside.setColor(MethodChecker.getColor(it,
                com.tokopedia.unifyprinciples.R.color.Unify_NN0)) }
            filterName?.context?.let { filterName?.setTextColor(MethodChecker.getColor(it,
                com.tokopedia.unifyprinciples.R.color.Unify_NN950)) }
        }
    }


}
