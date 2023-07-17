package com.tokopedia.csat_rating.quickfilter

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.csat_rating.R


open class ItemFilterViewHolder(itemView: View, listener: QuickSingleFilterListener?) : BaseItemFilterViewHolder(itemView, listener!!) {
    protected var layoutBorder: LinearLayout?
    protected var layoutInside: LinearLayout?
    protected var filterName: TextView?
    fun renderItemViewHolder(filterItem: QuickFilterItem) {
        updateData(filterItem)
        updateItemColor(filterItem.isSelected)
        itemView.setOnClickListener { listener.selectFilter(filterItem) }
    }

    protected open fun updateData(filterItem: QuickFilterItem) {
        filterName?.text = filterItem.name
        layoutBorder?.setBackgroundResource(R.drawable.csat_rating_bg_round_corner);
        layoutInside?.setBackgroundResource(R.drawable.csat_rating_bg_round_corner);
    }

    /**
     * @param selected using mutate for lock the drawable and set the color
     * documentation https://android-developers.googleblog.com/2009/05/drawable-mutations.html
     */
    protected open fun updateItemColor(selected: Boolean) {
        val drawableInside = layoutInside?.background?.current?.mutate() as GradientDrawable
        val drawableBorder = layoutBorder?.background?.current?.mutate() as GradientDrawable
        if (selected) {
            layoutBorder?.context?.let { drawableBorder.setColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_GN500)) }
            layoutInside?.context?.let { drawableInside.setColor(ContextCompat.getColor(it, R.color.csat_dms_reason_bg)) }
            filterName?.context?.let { filterName?.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_GN500)) }
        } else {
            layoutBorder?.context?.let { drawableBorder.setColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN200)) }
            layoutInside?.context?.let { drawableInside.setColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN0)) }
            filterName?.context?.let { filterName?.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68)) }
        }
    }

    init {
        layoutBorder = itemView.findViewById<View>(R.id.layout_border) as? LinearLayout
        layoutInside = itemView.findViewById<View>(R.id.layout_inside) as? LinearLayout
        filterName = itemView.findViewById<View>(R.id.filter_name) as? TextView
    }
}
